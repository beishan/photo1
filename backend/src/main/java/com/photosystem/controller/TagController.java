package com.photosystem.controller;

import com.photosystem.dto.ApiResponse;
import com.photosystem.dto.PhotoDTO;
import com.photosystem.dto.TagDTO;
import com.photosystem.entity.User;
import com.photosystem.repository.UserRepository;
import com.photosystem.service.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;
    private final UserRepository userRepository;

    public TagController(TagService tagService, UserRepository userRepository) {
        this.tagService = tagService;
        this.userRepository = userRepository;
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TagDTO>>> searchTags(
            @RequestParam String keyword,
            Pageable pageable) {
        try {
            Page<TagDTO> tags = tagService.searchTags(keyword, pageable)
                    .map(TagDTO::fromEntity);
            return ResponseEntity.ok(ApiResponse.success("Tags retrieved successfully", tags));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/photos/{photoId}")
    public ResponseEntity<ApiResponse<PhotoDTO>> addTagsToPhoto(
            @PathVariable Long photoId,
            @RequestBody Set<String> tagNames,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            PhotoDTO photo = PhotoDTO.fromEntity(tagService.addTagsToPhoto(photoId, tagNames, user));
            return ResponseEntity.ok(ApiResponse.success("Tags added to photo successfully", photo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/photos/{photoId}")
    public ResponseEntity<ApiResponse<PhotoDTO>> removeTagsFromPhoto(
            @PathVariable Long photoId,
            @RequestBody Set<String> tagNames,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            PhotoDTO photo = PhotoDTO.fromEntity(tagService.removeTagsFromPhoto(photoId, tagNames, user));
            return ResponseEntity.ok(ApiResponse.success("Tags removed from photo successfully", photo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/photos/{photoId}")
    public ResponseEntity<ApiResponse<Set<TagDTO>>> getPhotoTags(
            @PathVariable Long photoId) {
        try {
            Set<TagDTO> tags = tagService.getPhotoTags(photoId).stream()
                    .map(TagDTO::fromEntity)
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(ApiResponse.success("Photo tags retrieved successfully", tags));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}