package com.photosystem.dto;

import com.photosystem.entity.ShareLink;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShareLinkDTO {
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private boolean isPasswordProtected;
    private PhotoDTO photo;
    private AlbumDTO album;

    public static ShareLinkDTO fromEntity(ShareLink shareLink) {
        ShareLinkDTO dto = new ShareLinkDTO();
        dto.setToken(shareLink.getToken());
        dto.setExpiresAt(shareLink.getExpiresAt());
        dto.setCreatedAt(shareLink.getCreatedAt());
        dto.setPasswordProtected(shareLink.isPasswordProtected());

        if (shareLink.getPhoto() != null) {
            dto.setPhoto(PhotoDTO.fromEntity(shareLink.getPhoto()));
        }
        if (shareLink.getAlbum() != null) {
            dto.setAlbum(AlbumDTO.fromEntity(shareLink.getAlbum()));
        }
        return dto;
    }
}