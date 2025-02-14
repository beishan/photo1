package com.photosystem.service;

import com.photosystem.entity.Photo;
import com.photosystem.entity.User;
import com.photosystem.repository.PhotoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PhotoService {

    private final PhotoRepository photoRepository;
    private final StorageService storageService;
    private final ImageProcessingService imageProcessingService;

    public PhotoService(PhotoRepository photoRepository, StorageService storageService,
            ImageProcessingService imageProcessingService) {
        this.photoRepository = photoRepository;
        this.storageService = storageService;
        this.imageProcessingService = imageProcessingService;
    }

    public Photo uploadPhoto(MultipartFile file, User user) throws IOException {
        String filename = storageService.store(file);
        imageProcessingService.generateThumbnails(filename);

        Photo photo = new Photo();
        photo.setUser(user);
        photo.setFileName(filename);
        photo.setOriginalFileName(file.getOriginalFilename());
        photo.setContentType(file.getContentType());
        photo.setFileSize(file.getSize());
        photo.setTakenAt(LocalDateTime.now()); // 这里应该从照片EXIF中获取实际拍摄时间

        return photoRepository.save(photo);
    }

    public void deletePhoto(Long photoId, User user) throws IOException {
        Photo photo = photoRepository.findByIdAndUser(photoId, user)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        storageService.delete(photo.getFileName());
        imageProcessingService.deleteThumbnails(photo.getFileName());
        photoRepository.delete(photo);
    }

    public Page<Photo> getUserPhotos(User user, Pageable pageable) {
        return photoRepository.findByUser(user, pageable);
    }

    public Page<Photo> getPhotosByTimeRange(User user, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return photoRepository.findByUserAndTakenAtBetween(user, start, end, pageable);
    }

    public Page<Photo> getPhotosByLocation(User user, Double latitude, Double longitude, Double radius,
            Pageable pageable) {
        // 使用Haversine公式计算距离
        return photoRepository.findNearbyPhotos(user.getId(), latitude, longitude, radius, pageable);
    }

    @Transactional
    public void deletePhotos(Set<Long> photoIds, User user) throws IOException {
        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findByIdAndUser(photoId, user)
                    .orElseThrow(() -> new RuntimeException("Photo not found: " + photoId));

            storageService.delete(photo.getFileName());
            imageProcessingService.deleteThumbnails(photo.getFileName());
        }
        photoRepository.deleteAllByIdInAndUser(photoIds, user);
    }

    @Transactional
    public List<Photo> uploadPhotos(List<MultipartFile> files, User user) throws IOException {
        List<Photo> uploadedPhotos = new ArrayList<>();

        for (MultipartFile file : files) {
            String filename = storageService.store(file);
            imageProcessingService.generateThumbnails(filename);

            Photo photo = new Photo();
            photo.setUser(user);
            photo.setFileName(filename);
            photo.setOriginalFileName(file.getOriginalFilename());
            photo.setContentType(file.getContentType());
            photo.setFileSize(file.getSize());
            photo.setTakenAt(LocalDateTime.now());

            uploadedPhotos.add(photoRepository.save(photo));
        }

        return uploadedPhotos;
    }

    @Transactional
    public List<Photo> movePhotosToAlbum(Set<Long> photoIds, Long albumId, User user) {
        Album album = albumRepository.findByIdAndUser(albumId, user)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        List<Photo> movedPhotos = new ArrayList<>();
        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findByIdAndUser(photoId, user)
                    .orElseThrow(() -> new RuntimeException("Photo not found: " + photoId));

            photo.getAlbums().add(album);
            album.getPhotos().add(photo);
            movedPhotos.add(photoRepository.save(photo));
        }

        return movedPhotos;
    }

    @Transactional
    public List<Photo> addTagsToPhotos(Set<Long> photoIds, Set<String> tagNames, User user) {
        List<Photo> updatedPhotos = new ArrayList<>();

        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findByIdAndUser(photoId, user)
                    .orElseThrow(() -> new RuntimeException("Photo not found: " + photoId));

            for (String tagName : tagNames) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(tagName);
                            return tagRepository.save(newTag);
                        });

                photo.getTags().add(tag);
                tag.getPhotos().add(photo);
            }

            updatedPhotos.add(photoRepository.save(photo));
        }

        return updatedPhotos;
    }
}