package com.greamz.backend.controller;

import com.greamz.backend.dto.FileUpload;
import com.greamz.backend.service.ImageService;
import com.greamz.backend.service.MovieService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class FileControllerTest {
    @Mock
    private ImageService imageService;

    @Mock
    private MovieService movieService;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadImage() {
        MultipartFile mockFile = Mockito.mock(MultipartFile.class);
        FileUpload fileUpload = new FileUpload();
        fileUpload.setFile(mockFile);

        // Mocking the behavior of imageService.uploadImage()
        Optional<Map> uploadResult = Optional.of(new HashMap<>()); // Thay thế bằng dữ liệu phù hợp từ Cloudinary nếu cần
        when(imageService.uploadImage(any(FileUpload.class))).thenReturn(uploadResult);

        Optional<Map> response = imageService.uploadImage(fileUpload);
        assertTrue(response.isPresent());
        // Add more assertions as needed
    }

    @Test
    public void testUploadVideo() {
        MockMultipartFile file = new MockMultipartFile("file", "test-video.mp4", "video/mp4", "test data".getBytes());

        Map<String, Object> uploadResult = new HashMap<>();
        // Mocking the behavior of movieService.uploadMovie()
        when(movieService.uploadMovie(any(FileUpload.class))).thenReturn(Optional.of(uploadResult));

        ResponseEntity<?> response = fileController.uploadVideo(file);
        assertEquals(200, response.getStatusCodeValue());
        // Add more assertions as needed
    }

//    @Test
//    public void testFetchFile() {
//        String fileUrl = "http://example.com/test-file.txt";
//        byte[] testFileData = "Test file content".getBytes();
//
//        // Khởi tạo mock restTemplate
//        RestTemplate restTemplate = Mockito.mock(RestTemplate.class);
//        // Set up mock response
//        Mockito.when(restTemplate.getForObject(fileUrl, byte[].class)).thenReturn(testFileData);
//
//        // Khởi tạo fileController với restTemplate đã mock
//        FileController fileController = new FileController(imageService, movieService);
//
//        // Thực hiện gọi fetchFile
//        ResponseEntity<byte[]> response = fileController.fetchFile(fileUrl);
//
//        // Kiểm tra kết quả
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
}
