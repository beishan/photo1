package com.photosystem.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageService {

    @Value("${storage.photos}")
    private String uploadDir;

    public String store(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path destinationFile = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), destinationFile);

        return filename;
    }

    public void delete(String filename) throws IOException {
        Path file = Paths.get(uploadDir).resolve(filename);
        Files.deleteIfExists(file);
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf("."));
    }
}