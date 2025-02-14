package com.photosystem.controller;

import com.photosystem.dto.ApiResponse;
import com.photosystem.dto.PhotoDTO;
import com.photosystem.entity.User;
import com.photosystem.repository.UserRepository;
import com.photosystem.service.PhotoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    private final PhotoService photoService;
    private final UserRepository userRepository;

    public PhotoController(PhotoService photoService, UserRepository userRepository) {
        this.photoService = photoService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhotoDTO>> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            PhotoDTO photoDTO = PhotoDTO.fromEntity(photoService.uploadPhoto(file, user));
            return ResponseEntity.ok(ApiResponse.success("Photo uploaded successfully", photoDTO));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePhoto(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            photoService.deletePhoto(id, user);
            return ResponseEntity.ok(ApiResponse.success("Photo deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PhotoDTO>>> getUserPhotos(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<PhotoDTO> photos = photoService.getUserPhotos(user, pageable)
                    .map(PhotoDTO::fromEntity);
            return ResponseEntity.ok(ApiResponse.success("Photos retrieved successfully", photos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/timeline")
    public ResponseEntity<ApiResponse<Page<PhotoDTO>>> getPhotosByTimeRange(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Pageable pageable) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<PhotoDTO> photos = photoService.getPhotosByTimeRange(user, start, end, pageable)
                    .map(PhotoDTO::fromEntity);
            return ResponseEntity.ok(ApiResponse.success("Photos retrieved successfully", photos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/nearby")
    public ResponseEntity<ApiResponse<Page<PhotoDTO>>> getNearbyPhotos(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam(defaultValue = "5.0") Double radius,
            Pageable pageable) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<PhotoDTO> photos = photoService.getPhotosByLocation(user, latitude, longitude, radius, pageable)
                    .map(PhotoDTO::fromEntity);
            return ResponseEntity.ok(ApiResponse.success("Photos retrieved successfully", photos));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}