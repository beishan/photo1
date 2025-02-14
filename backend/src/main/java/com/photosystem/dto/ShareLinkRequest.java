package com.photosystem.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ShareLinkRequest {
    private Long photoId;
    private Long albumId;
    private LocalDateTime expiresAt;
    private String password;
}