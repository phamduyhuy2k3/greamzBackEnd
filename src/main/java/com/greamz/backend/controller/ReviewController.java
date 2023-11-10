package com.greamz.backend.controller;


import com.greamz.backend.model.Review;
import com.greamz.backend.model.Voucher;
import com.greamz.backend.service.ReviewService;
import com.greamz.backend.service.VoucherModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService service;
    @GetMapping("/findALl")
    public ResponseEntity<Iterable<Review>> findAll(){
        List<Review> reviews = service.findAll();
        return ResponseEntity.ok(reviews);
    }
    @GetMapping("/findAllPagination")
    public ResponseEntity<?> findAllPagination(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "7") int size) {
        return ResponseEntity.ok(service.findAll(page, size));
    }



    @GetMapping("/findById/{id}")
    public ResponseEntity<Review> findByid(@PathVariable("id") Long id){
        try {
            Review reviews = service.findReviewByid(id);
            return ResponseEntity.ok(reviews);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Review review){
        return ResponseEntity.ok().body(service.saveReviewModel(review));
    }

    @PutMapping("/update")
    public Review update(@RequestBody Review review){
        service.updateReviewModel(review);
        return review;
    }
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Long id){
        service.deleteReviewByAppid(id);
    }

    @GetMapping("{id}")
    public Review getOne(@PathVariable("id") Long appid) {
        return service.findByid(appid);
    }
}
