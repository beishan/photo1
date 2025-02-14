package com.photosystem.service;

import com.photosystem.entity.Photo;
import com.photosystem.entity.Tag;
import com.photosystem.entity.User;
import com.photosystem.repository.PhotoRepository;
import com.photosystem.repository.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PhotoRepository photoRepository;

    public TagService(TagRepository tagRepository, PhotoRepository photoRepository) {
        this.tagRepository = tagRepository;
        this.photoRepository = photoRepository;
    }

    public Tag createTag(String tagName) {
        if (tagRepository.existsByName(tagName)) {
            return tagRepository.findByName(tagName)
                    .orElseThrow(() -> new RuntimeException("Tag not found"));
        }

        Tag tag = new Tag();
        tag.setName(tagName);
        return tagRepository.save(tag);
    }

    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
        tagRepository.delete(tag);
    }

    public Page<Tag> searchTags(String keyword, Pageable pageable) {
        return tagRepository.searchByKeyword(keyword, pageable);
    }

    @Transactional
    public Photo addTagsToPhoto(Long photoId, Set<String> tagNames, User user) {
        Photo photo = photoRepository.findByIdAndUser(photoId, user)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> createTag(tagName));
            tags.add(tag);
        }

        photo.getTags().addAll(tags);
        for (Tag tag : tags) {
            tag.getPhotos().add(photo);
        }

        return photoRepository.save(photo);
    }

    @Transactional
    public Photo removeTagsFromPhoto(Long photoId, Set<String> tagNames, User user) {
        Photo photo = photoRepository.findByIdAndUser(photoId, user)
                .orElseThrow(() -> new RuntimeException("Photo not found"));

        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseThrow(() -> new RuntimeException("Tag not found: " + tagName));
            photo.getTags().remove(tag);
            tag.getPhotos().remove(photo);
        }

        return photoRepository.save(photo);
    }

    public Set<Tag> getPhotoTags(Long photoId) {
        return tagRepository.findByPhotoId(photoId);
    }
}