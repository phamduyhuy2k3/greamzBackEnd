package com.greamz.backend.controller;

import com.greamz.backend.model.Review;
import com.greamz.backend.service.ReviewService;
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
import java.util.NoSuchElementException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReviewControllerTest {
    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewController reviewController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFindAll() {
        // Test for findAll method
        List<Review> mockReviews = Arrays.asList(new Review(/*mock parameters*/), new Review(/*mock parameters*/));
        when(reviewService.findAll()).thenReturn(mockReviews);

        ResponseEntity<Iterable<Review>> response = reviewController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testFindAllPagination() {
        // Test for findAllPagination method
        Page<Review> page = new PageImpl<>(Collections.emptyList());
        when(reviewService.findAll()).thenReturn((List<Review>) page);
        ResponseEntity<?> response = reviewController.findAllPagination(0, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreate() {
        Review review = new Review(/*mock parameters*/);
        when(reviewService.saveReviewModel(review)).thenReturn(review);

        ResponseEntity<?> response = reviewController.create(review);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetOne() {
        Long id = 1L;
        Review review = new Review(/*mock parameters*/);
        when(reviewService.findByid(id)).thenReturn(review);

        Review response = reviewController.getOne(id);

        assertEquals(review, response);
    }

    @Test
    public void testFindById() {
        long id = 1L;
        Review review = new Review();
        review.setId(id);
        when(reviewService.findReviewByid(id)).thenReturn(review);

        ResponseEntity<Review> responseEntity = reviewController.findByid(id);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(review, responseEntity.getBody());

    }

    @Test
    public void testDelete() {
        long id = 1L;
        doNothing().when(reviewService).deleteReviewByAppid(id);

        reviewController.delete(id);

        verify(reviewService, times(1)).deleteReviewByAppid(id);
    }

    @Test
    public void testUpdate() {
        Review review = new Review(/*mock parameters*/);
        when(reviewService.saveReviewModel(review)).thenReturn(review);

        Review response = reviewController.update(review);

        assertEquals(review, response);
    }
}
