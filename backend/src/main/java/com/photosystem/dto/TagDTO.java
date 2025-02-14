package com.photosystem.dto;

import com.photosystem.entity.Tag;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class TagDTO {
    private Long id;

    @NotBlank(message = "Tag name cannot be empty")
    private String name;

    private List<PhotoDTO> photos;

    public static TagDTO fromEntity(Tag tag) {
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        if (tag.getPhotos() != null) {
            dto.setPhotos(tag.getPhotos().stream()
                    .map(PhotoDTO::fromEntity)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}