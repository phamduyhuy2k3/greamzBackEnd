package com.greamz.backend.repository;

import com.greamz.backend.model.Review;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findAllByGameAppid(Long appid);
}
