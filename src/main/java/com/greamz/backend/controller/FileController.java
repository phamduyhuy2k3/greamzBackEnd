package com.greamz.backend.controller;

import com.greamz.backend.dto.FileUpload;
import com.greamz.backend.service.ImageService;
import com.greamz.backend.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    private final ImageService imageService;
    private final MovieService movieService;
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile(file);
        return ResponseEntity.ok(imageService.uploadImage(fileUpload));
    }
    @PostMapping("/video")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile(file);
        return ResponseEntity.ok(movieService.uploadMovie(fileUpload));
    }
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/fetch-file")
    public ResponseEntity<byte[]> fetchFile(@RequestParam("fileUrl") String fileUrl) {
        try {
            // Fetch the file from the URL as a byte array
            byte[] fileData = restTemplate.getForObject(fileUrl, byte[].class);

            // Return the byte array as a response
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment;")
                    .body(fileData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new byte[0]);
        }
    }
}
