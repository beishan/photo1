package com.photosystem.service;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageProcessingService {

    @Value("${storage.photos}")
    private String photoDir;

    @Value("${storage.thumbnails}")
    private String thumbnailDir;

    private static final int THUMBNAIL_SIZE = 300;
    private static final int MEDIUM_SIZE = 800;
    private static final String THUMBNAIL_PREFIX = "thumb_";
    private static final String MEDIUM_PREFIX = "medium_";

    public void generateThumbnails(String filename) throws IOException {
        Path originalPath = Paths.get(photoDir, filename);
        Path thumbnailPath = Paths.get(thumbnailDir);

        if (!Files.exists(thumbnailPath)) {
            Files.createDirectories(thumbnailPath);
        }

        // Generate square thumbnail
        Thumbnails.of(originalPath.toFile())
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .crop(Positions.CENTER)
                .toFile(new File(thumbnailPath.toString(), THUMBNAIL_PREFIX + filename));

        // Generate medium size image
        Thumbnails.of(originalPath.toFile())
                .size(MEDIUM_SIZE, MEDIUM_SIZE)
                .keepAspectRatio(true)
                .toFile(new File(thumbnailPath.toString(), MEDIUM_PREFIX + filename));
    }

    public void deleteThumbnails(String filename) throws IOException {
        Path thumbnailPath = Paths.get(thumbnailDir);
        Files.deleteIfExists(thumbnailPath.resolve(THUMBNAIL_PREFIX + filename));
        Files.deleteIfExists(thumbnailPath.resolve(MEDIUM_PREFIX + filename));
    }

    public String getThumbnailPath(String filename) {
        return Paths.get(thumbnailDir, THUMBNAIL_PREFIX + filename).toString();
    }

    public String getMediumPath(String filename) {
        return Paths.get(thumbnailDir, MEDIUM_PREFIX + filename).toString();
    }
}