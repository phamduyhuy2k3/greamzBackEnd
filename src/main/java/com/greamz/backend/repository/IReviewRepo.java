package com.greamz.backend.repository;

import com.greamz.backend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IReviewRepo extends JpaRepository<Review, Long> {
}
