package com.greamz.backend.service;

import com.greamz.backend.dto.account.AccountBasicDTO;
import com.greamz.backend.dto.review.ReviewsUserDTO;
import com.greamz.backend.model.Review;
import com.greamz.backend.repository.IReviewRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {
    private final IReviewRepo repo;

    public Review saveReviewModel(Review reviewModel) {
        return repo.save(reviewModel);
    }

    @Transactional
    public void updateReviewModel(Review reviewModel) {
        Review review = repo.findById(reviewModel.getId()).orElseThrow();
        repo.save(reviewModel);
    }

    @Transactional(readOnly = true)
    public List<Review> findAll() {
        List<Review> reviews = repo.findAll();
        reviews.forEach(review -> {
            review.setCreatedAt(null);
            review.setUpdatedAt(null);

//            review.setGame(null);
//            review.setAccount(null);
            Hibernate.initialize(review.getGame());
            Hibernate.initialize(review.getAccount());
        });
        return reviews;
    }

    @Transactional(readOnly = true)
    public List<ReviewsUserDTO> findReviewByGame(Long gameAppId) {
        List<Review> reviewsList = repo.findAllByGameAppid(gameAppId);
        List<ReviewsUserDTO> reviewsUserDTOList  = reviewsList.stream()
                .map(review -> {
                    Hibernate.initialize(review.getAccount());
                    ReviewsUserDTO reviewsUserDTO = new ReviewsUserDTO();
                    reviewsUserDTO.setAccount(
                            AccountBasicDTO.builder()
                                    .id(review.getAccount().getId())
                                    .photo(review.getAccount().getPhoto())
                                    .username(review.getAccount().getUsername())
                                    .build()
                    );
                    reviewsUserDTO.setDislike(review.getDislikes());
                    reviewsUserDTO.setLike(review.getLikes());
                    reviewsUserDTO.setRating(review.getRating());
                    reviewsUserDTO.setText(review.getText());
                    return reviewsUserDTO;
                }).toList();


        return reviewsUserDTOList;
    }

    @Transactional(readOnly = true)
    public Page<Review> findAll(Pageable pageable) {
        Page<Review> reviewPage = repo.findAll(pageable);
        reviewPage.forEach(review ->
                {
                    review.setCreatedAt(null);
                    review.setUpdatedAt(null);
                    review.setGame(null);
                    review.setAccount(null);
//                    Hibernate.initialize(review.getAccount());
//                    Hibernate.initialize(review.getGame());
                }

        );
        return reviewPage;
    }

    @Transactional
    public void deleteReviewByAppid(Long id) {
        Review reviewModel = repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found review with id: " + id));
        repo.deleteById(id);

    }

    @Transactional
    public Review findReviewByid(Long id) throws NoSuchElementException {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found Review with id: " + id));
    }

    @Transactional
    public Review findByid(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found Review with id: " + id));
    }
}
