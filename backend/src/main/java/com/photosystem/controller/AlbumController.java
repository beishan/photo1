package com.photosystem.controller;

import com.photosystem.dto.AlbumDTO;
import com.photosystem.dto.ApiResponse;
import com.photosystem.entity.Album;
import com.photosystem.entity.User;
import com.photosystem.repository.UserRepository;
import com.photosystem.service.AlbumService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final UserRepository userRepository;

    public AlbumController(AlbumService albumService, UserRepository userRepository) {
        this.albumService = albumService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AlbumDTO>> createAlbum(
            @Valid @RequestBody AlbumDTO albumDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Album album = new Album();
            album.setName(albumDTO.getName());
            album.setDescription(albumDTO.getDescription());

            AlbumDTO createdAlbum = AlbumDTO.fromEntity(albumService.createAlbum(album, user));
            return ResponseEntity.ok(ApiResponse.success("Album created successfully", createdAlbum));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumDTO>> updateAlbum(
            @PathVariable Long id,
            @Valid @RequestBody AlbumDTO albumDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Album album = new Album();
            album.setName(albumDTO.getName());
            album.setDescription(albumDTO.getDescription());

            AlbumDTO updatedAlbum = AlbumDTO.fromEntity(albumService.updateAlbum(id, album, user));
            return ResponseEntity.ok(ApiResponse.success("Album updated successfully", updatedAlbum));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            albumService.deleteAlbum(id, user);
            return ResponseEntity.ok(ApiResponse.success("Album deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AlbumDTO>>> getUserAlbums(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<AlbumDTO> albums = albumService.getUserAlbums(user, pageable)
                    .map(AlbumDTO::fromEntity);
            return ResponseEntity.ok(ApiResponse.success("Albums retrieved successfully", albums));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlbumDTO>> getAlbum(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AlbumDTO album = AlbumDTO.fromEntity(albumService.getAlbum(id, user));
            return ResponseEntity.ok(ApiResponse.success("Album retrieved successfully", album));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/photos")
    public ResponseEntity<ApiResponse<AlbumDTO>> addPhotosToAlbum(
            @PathVariable Long id,
            @RequestBody Set<Long> photoIds,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AlbumDTO album = AlbumDTO.fromEntity(albumService.addPhotosToAlbum(id, photoIds, user));
            return ResponseEntity.ok(ApiResponse.success("Photos added to album successfully", album));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}/photos")
    public ResponseEntity<ApiResponse<AlbumDTO>> removePhotosFromAlbum(
            @PathVariable Long id,
            @RequestBody Set<Long> photoIds,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AlbumDTO album = AlbumDTO.fromEntity(albumService.removePhotosFromAlbum(id, photoIds, user));
            return ResponseEntity.ok(ApiResponse.success("Photos removed from album successfully", album));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}