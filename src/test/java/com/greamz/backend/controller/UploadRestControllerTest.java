package com.greamz.backend.controller;

import com.greamz.backend.service.FilesService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UploadRestControllerTest {
    @Mock
    private FilesService filesService;

    @InjectMocks
    private UploadRestController uploadRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUploadFile_Success() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(filesService.save(mockFile)).thenReturn("http://example.com/uploaded-file.txt");

        ResponseEntity<UploadRestController.FileUploadResponse> response = uploadRestController.uploadFile(mockFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("http://example.com/uploaded-file.txt", response.getBody().uploadUrl());
        verify(filesService, times(1)).save(mockFile);
    }

    @Test
    public void testUploadFile_EmptyFile() {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(true);

        ResponseEntity<UploadRestController.FileUploadResponse> response = uploadRestController.uploadFile(mockFile);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("File is empty", response.getBody().uploadUrl());
        verifyNoInteractions(filesService);
    }

    @Test
    public void testUploadFile_Failure() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(filesService.save(mockFile)).thenThrow(new RuntimeException("Failed to save file"));

        ResponseEntity<UploadRestController.FileUploadResponse> response = uploadRestController.uploadFile(mockFile);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to upload file", response.getBody().uploadUrl());
        verify(filesService, times(1)).save(mockFile);
    }

}
