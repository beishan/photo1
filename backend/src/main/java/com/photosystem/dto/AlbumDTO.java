package com.photosystem.dto;

import com.photosystem.entity.Album;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AlbumDTO {
    private Long id;

    @NotBlank(message = "Album name cannot be empty")
    private String name;

    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PhotoDTO> photos;

    public static AlbumDTO fromEntity(Album album) {
        AlbumDTO dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setDescription(album.getDescription());
        dto.setCreatedAt(album.getCreatedAt());
        dto.setUpdatedAt(album.getUpdatedAt());
        if (album.getPhotos() != null) {
            dto.setPhotos(album.getPhotos().stream()
                    .map(PhotoDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}