package com.photosystem.controller;

import com.photosystem.service.ImageProcessingService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageProcessingService imageProcessingService;

    public ImageController(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    @GetMapping("/thumbnail/{filename}")
    public ResponseEntity<Resource> getThumbnail(@PathVariable String filename) {
        Path thumbnailPath = Paths.get(imageProcessingService.getThumbnailPath(filename));
        Resource resource = new FileSystemResource(thumbnailPath);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/medium/{filename}")
    public ResponseEntity<Resource> getMediumImage(@PathVariable String filename) {
        Path mediumPath = Paths.get(imageProcessingService.getMediumPath(filename));
        Resource resource = new FileSystemResource(mediumPath);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}