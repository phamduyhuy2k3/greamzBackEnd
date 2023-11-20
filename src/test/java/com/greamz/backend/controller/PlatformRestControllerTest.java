package com.greamz.backend.controller;

import com.greamz.backend.enumeration.Devices;
import com.greamz.backend.model.Category;
import com.greamz.backend.model.Platform;
import com.greamz.backend.service.PlatformService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlatformRestControllerTest {
    @Mock
    private PlatformService platformService;

    @InjectMocks
    private PlatformRestController platformRestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAllByDevice() {
        Devices device = Devices.PC;
        Set<Platform> platforms = new HashSet<>();
        when(platformService.findAllByDevices(device)).thenReturn(platforms);

        ResponseEntity<Set<Platform>> response = platformRestController.findAllByDevice(device);

        assertEquals(platforms, response.getBody());
    }

//    @Test
//    public void testFindPlatformByDevice() {
//        List<String> deviceNames = Arrays.asList("DESKTOP", "MOBILE"); // List tên các device
//        when(platformService.findPlatformByDevice()).thenReturn(deviceNames);
//
//        ResponseEntity<List<String>> response = platformRestController.findPlatformByDevice();
//
//        assertEquals(deviceNames, response.getBody());
//    }

    @Test
    public void testFindAll() {
        List<Platform> platforms = Collections.emptyList(); // Danh sách platform rỗng
        when(platformService.findAll()).thenReturn(platforms);

        ResponseEntity<List<Platform>> response = platformRestController.findAll();

        assertEquals(platforms, response.getBody());
    }

    @Test
    public void testGetOne() {
        Integer id = 1;
        Platform platform = new Platform(); // Tạo đối tượng Platform
        when(platformService.findById(id)).thenReturn(platform);

        ResponseEntity<Platform> response = platformRestController.getOne(id);

        assertEquals(platform, response.getBody());
    }

    @Test
    public void testDelete() {
        Integer id = 1;
        // Không cần trả về giá trị khi gọi phương thức deletePlatform, chỉ cần kiểm tra xem nó có ném ngoại lệ hay không
        assertDoesNotThrow(() -> platformRestController.delete(id));
        verify(platformService, times(1)).deletePlatform(id);
    }

    @Test
    public void testSave() throws SQLIntegrityConstraintViolationException {
        Platform platform = new Platform(); // Tạo đối tượng Platform
        when(platformService.savePlatform(platform)).thenReturn(platform);

        ResponseEntity<?> response = platformRestController.save(platform);

        assertEquals(platform, response.getBody());
    }

    @Test
    public void testSave_WithSQLIntegrityConstraintViolationException() throws SQLIntegrityConstraintViolationException {
        Platform platform = new Platform(); // Tạo đối tượng Platform
        doThrow(SQLIntegrityConstraintViolationException.class).when(platformService).savePlatform(platform);

        ResponseEntity<?> response = platformRestController.save(platform);

        assertEquals("Platform with name: " + platform.getName() + " already exist", response.getBody());
    }

    @Test
    public void testFindAllPagination() {
        int page = 1;
        int size = 5;

        List<Platform> mockPlatformsList = Arrays.asList(new Platform(/*mock parameters*/), new Platform(/*mock parameters*/));
        Page<Platform> mockPlatformsPage = new PageImpl<>(mockPlatformsList);
        when(platformService.findAll(page, size)).thenReturn(mockPlatformsPage);

        ResponseEntity<?> response = platformRestController.findAllPagination(page, size);

        TestCase.assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        // Add more assertions as needed
    }

}
