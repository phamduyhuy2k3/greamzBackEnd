package com.greamz.backend.controller;


import com.greamz.backend.dto.review.ReviewFromUser;
import com.greamz.backend.dto.review.ReviewOfGame;
import com.greamz.backend.dto.review.ReviewsUserDTO;
import com.greamz.backend.dto.review.reaction.ReactResponse;
import com.greamz.backend.dto.review.reaction.UserReactTheReview;
import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Review;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService service;

    @GetMapping("/findALl")
    public ResponseEntity<Iterable<Review>> findAll() {
        List<Review> reviews = service.findAll();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/findAllPagination")
    public ResponseEntity<Page<Review>> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "7") int size) {
        return ResponseEntity.ok(service.findAll(PageRequest.of(page, size)));
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<Review> findByid(@PathVariable("id") Long id) {
        try {
            Review reviews = service.findReviewByid(id);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/findByGame/{appid}")
    public ResponseEntity<List<ReviewsUserDTO>> findByGame(@PathVariable("appid") Long id) {
        try {
            List<ReviewsUserDTO> reviews = service.findReviewByGame(id);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Review review) {
        return ResponseEntity.ok().body(service.saveReviewModel(review));
    }

    @PutMapping("/update")
    public Review update(@RequestBody Review review) {
        service.updateReviewModel(review);

        return review;
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.deleteReviewByAppid(id);
    }

    @GetMapping("{id}")
    public Review getOne(@PathVariable("id") Long appid) {
        return service.findByid(appid);
    }
    @PostMapping("/user/review")
    public ResponseEntity<?> createReview(@RequestBody ReviewFromUser review, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        review.setAccountId(userPrincipal.getId());
        return ResponseEntity.ok().body(service.saveReviewOfUser(review));
    }
    @PostMapping("/react")
    public ResponseEntity<ReactResponse> likeReview(@RequestBody UserReactTheReview userReactTheReview) {
        log.info("userReactTheReview:"+userReactTheReview);
       return ResponseEntity.ok(service.reactReview(userReactTheReview));
    }

}
