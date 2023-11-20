package com.greamz.backend.controller;

import com.greamz.backend.model.Review;
import com.greamz.backend.model.Voucher;
import com.greamz.backend.service.VoucherModelService;
import org.junit.Assert;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class VoucherRestControllerTest {
    @Mock
    private VoucherModelService voucherModelService;

    @InjectMocks
    private VoucherRestController voucherRestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        // Test for findAll method
        List<Voucher> mockVouchers = Arrays.asList(new Voucher(/*mock parameters*/), new Voucher(/*mock parameters*/));
        when(voucherModelService.findAll()).thenReturn(mockVouchers);

        ResponseEntity<Iterable<Voucher>> response = voucherRestController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testFindAllPagination() {
        // Test for findAllPagination method
        Page<Voucher> page = new PageImpl<>(Collections.emptyList());
        when(voucherModelService.findAll(anyInt(), anyInt())).thenReturn(page);

        ResponseEntity<?> response = voucherRestController.findAllPagination(0, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreate() {
        Voucher voucher = new Voucher(/*mock parameters*/);
        when(voucherModelService.saveVoucherModel(voucher)).thenReturn(voucher);

        ResponseEntity<?> response = voucherRestController.create(voucher);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOne() {
        Long id = 1L;
        Voucher voucher = new Voucher(/*mock parameters*/);
        when(voucherModelService.findByid(id)).thenReturn(voucher);

        Voucher response = voucherRestController.getOne(id);

        assertEquals(voucher, response);
    }

    @Test
    public void testFindById() {
        long id = 1L;
        Voucher voucher = new Voucher();
        when(voucherModelService.findVoucherByid(id)).thenReturn(voucher);

        ResponseEntity<Voucher> response = voucherRestController.findByid(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(voucher, response.getBody());

    }

    @Test
    public void testDelete() {
        long id = 1L;
        doNothing().when(voucherModelService).deleteVoucherByAppid(id);
        voucherRestController.delete(id);

        verify(voucherModelService, times(1)).deleteVoucherByAppid(id);
    }

    @Test
    public void testUpdate() {
        Voucher voucher = new Voucher(/*mock parameters*/);
        voucherRestController.update(voucher);
        verify(voucherModelService, times(1)).updateVoucherModel(voucher);

    }
}
