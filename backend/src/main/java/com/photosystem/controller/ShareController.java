package com.photosystem.controller;

import com.photosystem.dto.*;
import com.photosystem.entity.ShareLink;
import com.photosystem.entity.User;
import com.photosystem.repository.UserRepository;
import com.photosystem.service.ShareService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/share")
public class ShareController {

    private final ShareService shareService;
    private final UserRepository userRepository;

    public ShareController(ShareService shareService, UserRepository userRepository) {
        this.shareService = shareService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ShareLinkDTO>> createShareLink(
            @RequestBody ShareLinkRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            ShareLink shareLink = shareService.createShareLink(
                    request.getPhotoId(),
                    request.getAlbumId(),
                    request.getExpiresAt(),
                    request.getPassword(),
                    user);

            return ResponseEntity.ok(ApiResponse.success(
                    "Share link created successfully",
                    ShareLinkDTO.fromEntity(shareLink)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/links")
    public ResponseEntity<ApiResponse<Page<ShareLinkDTO>>> getUserShareLinks(
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Page<ShareLinkDTO> shareLinks = shareService.getUserShareLinks(user, pageable)
                    .map(ShareLinkDTO::fromEntity);

            return ResponseEntity.ok(ApiResponse.success(
                    "Share links retrieved successfully",
                    shareLinks));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{token}")
    public ResponseEntity<ApiResponse<Void>> deleteShareLink(
            @PathVariable String token,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            shareService.deleteShareLink(token, user);
            return ResponseEntity.ok(ApiResponse.success("Share link deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/public/{token}")
    public ResponseEntity<ApiResponse<ShareLinkDTO>> getSharedContent(
            @PathVariable String token,
            @RequestParam(required = false) String password) {
        try {
            ShareLink shareLink = shareService.getShareLink(token, password);
            return ResponseEntity.ok(ApiResponse.success(
                    "Shared content retrieved successfully",
                    ShareLinkDTO.fromEntity(shareLink)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}