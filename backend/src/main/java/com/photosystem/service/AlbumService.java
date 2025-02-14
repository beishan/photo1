package com.photosystem.service;

import com.photosystem.entity.Album;
import com.photosystem.entity.Photo;
import com.photosystem.entity.User;
import com.photosystem.repository.AlbumRepository;
import com.photosystem.repository.PhotoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final PhotoRepository photoRepository;

    public AlbumService(AlbumRepository albumRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.photoRepository = photoRepository;
    }

    public Album createAlbum(Album album, User user) {
        if (albumRepository.existsByNameAndUser(album.getName(), user)) {
            throw new RuntimeException("Album with this name already exists");
        }
        album.setUser(user);
        return albumRepository.save(album);
    }

    public Album updateAlbum(Long albumId, Album albumDetails, User user) {
        Album album = albumRepository.findByIdAndUser(albumId, user)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        if (!album.getName().equals(albumDetails.getName()) &&
                albumRepository.existsByNameAndUser(albumDetails.getName(), user)) {
            throw new RuntimeException("Album with this name already exists");
        }

        album.setName(albumDetails.getName());
        album.setDescription(albumDetails.getDescription());
        return albumRepository.save(album);
    }

    public void deleteAlbum(Long albumId, User user) {
        Album album = albumRepository.findByIdAndUser(albumId, user)
                .orElseThrow(() -> new RuntimeException("Album not found"));
        albumRepository.delete(album);
    }

    public Page<Album> getUserAlbums(User user, Pageable pageable) {
        return albumRepository.findByUser(user, pageable);
    }

    public Album getAlbum(Long albumId, User user) {
        return albumRepository.findByIdAndUser(albumId, user)
                .orElseThrow(() -> new RuntimeException("Album not found"));
    }

    @Transactional
    public Album addPhotosToAlbum(Long albumId, Set<Long> photoIds, User user) {
        Album album = albumRepository.findByIdAndUser(albumId, user)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findByIdAndUser(photoId, user)
                    .orElseThrow(() -> new RuntimeException("Photo not found: " + photoId));
            album.getPhotos().add(photo);
            photo.getAlbums().add(album);
        }

        return albumRepository.save(album);
    }

    @Transactional
    public Album removePhotosFromAlbum(Long albumId, Set<Long> photoIds, User user) {
        Album album = albumRepository.findByIdAndUser(albumId, user)
                .orElseThrow(() -> new RuntimeException("Album not found"));

        for (Long photoId : photoIds) {
            Photo photo = photoRepository.findByIdAndUser(photoId, user)
                    .orElseThrow(() -> new RuntimeException("Photo not found: " + photoId));
            album.getPhotos().remove(photo);
            photo.getAlbums().remove(album);
        }

        return albumRepository.save(album);
    }
}