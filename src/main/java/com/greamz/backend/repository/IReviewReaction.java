package com.greamz.backend.repository;

import com.greamz.backend.model.AccountModel;
import com.greamz.backend.model.Review;
import com.greamz.backend.model.ReviewReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IReviewReaction extends JpaRepository<ReviewReaction, Long> {
    @Query("""
            SELECT 
            (SELECT COUNT(rr) FROM ReviewReaction rr WHERE rr.review = :review AND rr.reactionType = 'LIKE') AS numberOfLikes, 
            (SELECT COUNT(rr) FROM ReviewReaction rr WHERE rr.review = :review AND rr.reactionType = 'DISLIKE') AS numberOfDislikes 
            FROM ReviewReaction rr WHERE rr.review = :review
            """)
    Object countLikesAndDislikesForReview(@Param("review") Review review);

    @Query("SELECT COUNT(rr) FROM ReviewReaction rr WHERE rr.review.id = :review AND rr.reactionType = 'LIKE'")
    int countLikesForReview(@Param("review") Long review);

    @Query("SELECT COUNT(rr) FROM ReviewReaction rr WHERE rr.review.id = :review AND rr.reactionType = 'DISLIKE'")
    int countDislikesForReview(@Param("review") Long review);

    //isUSerReact

    Optional<ReviewReaction> findByUser_IdAndReview_Id(Integer userId, Long reviewId);
}
