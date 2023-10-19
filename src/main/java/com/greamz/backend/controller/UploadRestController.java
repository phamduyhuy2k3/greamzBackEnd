package com.greamz.backend.controller;


import com.greamz.backend.service.FilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class UploadRestController {
    private final FilesService uploadService;
    public record FileUploadResponse(String uploadUrl) {
    }
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Check if the file is not empty

            if (file.isEmpty()) {
                return new ResponseEntity<>(new FileUploadResponse("File is empty"), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>(new FileUploadResponse(uploadService.save(file)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileUploadResponse("Failed to upload file"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
