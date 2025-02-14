package com.photosystem.dto;

import com.photosystem.entity.Photo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhotoDTO {
    private Long id;
    private String fileName;
    private String originalFileName;
    private String thumbnailUrl;
    private String mediumUrl;
    private LocalDateTime takenAt;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;

    public static PhotoDTO fromEntity(Photo photo) {
        PhotoDTO dto = new PhotoDTO();
        dto.setId(photo.getId());
        dto.setFileName(photo.getFileName());
        dto.setOriginalFileName(photo.getOriginalFileName());
        dto.setTakenAt(photo.getTakenAt());
        dto.setLatitude(photo.getLatitude());
        dto.setLongitude(photo.getLongitude());
        dto.setCreatedAt(photo.getCreatedAt());
        return dto;
    }
}