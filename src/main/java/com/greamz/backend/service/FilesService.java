package com.greamz.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.UUID;

@Service
public class FilesService {
    private final ResourceLoader resourceLoader;

    @Autowired
    public FilesService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public String save(MultipartFile file) throws IOException {
        // Get the file name
        String fileName = "file_" + UUID.randomUUID() + new Date().toInstant() + ".jpg";
        fileName = fileName.replace(":", "");

        // Define the file path to save in resource/static/img directory
        Path filePath = Paths.get("src/main/resources/static/assets/img/game/"+fileName );

        // Copy the file to the specified location
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return the file URL
        return  filePath.getFileName().toString();
    }
    public void delete(String url) throws IOException {
        Path filePath = Paths.get("src/main/resources/static/assets/img/game/"+url);
        Files.delete(filePath);
    }
}
