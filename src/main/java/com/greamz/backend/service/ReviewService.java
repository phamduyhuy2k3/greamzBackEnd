package com.greamz.backend.service;

import com.greamz.backend.model.Review;
import com.greamz.backend.repository.IReviewRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {
    private final IReviewRepo repo;

    public Review saveReviewModel(Review reviewModel) {
        return repo.saveAndFlush(reviewModel);
    }

    @Transactional
    public void updateReviewModel(Review reviewModel) {
        Review review = repo.findById(reviewModel.getId()).orElseThrow();
        repo.save(reviewModel);
    }
    @Transactional
    public List<Review> findAll() {
        return repo.findAll();
    }
    @Transactional
    public Page<Review> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repo.findAll(pageable);
    }

    @Transactional
    public void deleteReviewByAppid(Long id){
        Review reviewModel = repo.findById(id).orElseThrow(()->new NoSuchElementException("Not found review with id: "+ id));
        repo.deleteById(id);

    }

    @Transactional
    public Review findReviewByid(Long id) throws NoSuchElementException {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found Review with id: " + id));
    }

    @Transactional
    public Review findByid(Long id){
        return repo.findById(id).orElseThrow(()->new NoSuchElementException("Not found Review with id: "+ id));
    }
}
