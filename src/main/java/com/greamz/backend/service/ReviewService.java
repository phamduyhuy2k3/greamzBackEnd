package com.greamz.backend.service;

import com.greamz.backend.dto.account.AccountBasicDTO;
import com.greamz.backend.dto.review.*;
import com.greamz.backend.dto.review.reaction.ReactResponse;
import com.greamz.backend.dto.review.reaction.UserReactTheReview;
import com.greamz.backend.enumeration.ReactionType;
import com.greamz.backend.model.*;
import com.greamz.backend.repository.IOrderDetail;
import com.greamz.backend.repository.IReviewReaction;
import com.greamz.backend.repository.IReviewRepo;
import com.greamz.backend.security.UserPrincipal;
import com.greamz.backend.util.Mapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReviewService {
    private final IReviewRepo repo;
    private final IOrderDetail orderDetailRepo;
    private final IReviewReaction reviewReactionRepo;
    public Review saveReviewModel(Review reviewModel) {
        return repo.save(reviewModel);
    }
    public ReviewBasic saveReviewOfUser(ReviewFromUser reviewFromUser) {
        Review reviewModel = Review.builder()
                .text(reviewFromUser.getText())
                .rating(reviewFromUser.getRating())
                .game(GameModel.builder().appid(reviewFromUser.getAppid()).build())
                .account(AccountModel.builder().id(reviewFromUser.getAccountId()).build())
                .build();
        Review review = repo.save(reviewModel);
        Optional<OrdersDetail> ordersDetail = orderDetailRepo.findById(reviewFromUser.getOrderDetailId());
        if (ordersDetail.isPresent()) {
            OrdersDetail ordersDetail1= ordersDetail.get();
            ordersDetail1.setReview(review);
            orderDetailRepo.save(ordersDetail1);
        }


        return Mapper.mapObject(review, ReviewBasic.class);
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

                    reviewsUserDTO.setRating(review.getRating());
                    reviewsUserDTO.setText(review.getText());
                    return reviewsUserDTO;
                }).toList();


        return reviewsUserDTOList;
    }
    @Transactional(readOnly = true)
    public Page<ReviewOfGame> findReviewOfGame(Long gameAppId, Pageable pageable, Integer userId) {
        Page<Review> reviewsList = repo.findAllByGameAppid(gameAppId, pageable);
        return reviewsList.map(review -> {
                    ReviewOfGame reviewsUserDTO = Mapper.mapObject(review, ReviewOfGame.class);
                    if(userId >-1 ){
                        Optional<ReviewReaction> optionalReviewReaction=reviewReactionRepo.findByUser_IdAndReview_Id(userId,review.getId());
                        if(optionalReviewReaction.isPresent()){
                            ReviewReaction reviewReaction = optionalReviewReaction.get();
                            reviewsUserDTO.setReactionType(reviewReaction.getReactionType());
                            reviewsUserDTO.setReacted(true);
                            log.info("reviewReaction.getReactionType()"+reviewReaction.getReactionType());
                        }else {
                            reviewsUserDTO.setReacted(false);
                            log.info("reviewReaction.getReactionType()"+null);
                        }
                    }
                    reviewsUserDTO.setLikes(reviewReactionRepo.countLikesForReview(review.getId()));
                    reviewsUserDTO.setDislikes(reviewReactionRepo.countDislikesForReview(review.getId()));
                   return reviewsUserDTO;
                });
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

    @Transactional(readOnly = true)
    public Review findByid(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found Review with id: " + id));
    }
    @Transactional(readOnly = true)
    public List<Review> findAllByAccountId(Integer id) {
        List<Review> reviews = repo.findAllByAccount_Id(id);
        reviews.forEach(review -> {
            review.setGame(null);
            review.setAccount(null);
        });
        return reviews;
    }
    @Transactional(readOnly = true)
    public List<Review> findAllReviewsByAccountId(Integer accountId) {
        List<Review> reviews = repo.findAllByAccount_Id(accountId);
        reviews.forEach(reviews1 -> {
            reviews1.setAccount(null);
            reviews1.setGame(null);
        });
        return reviews;
    }

    public ReactResponse reactReview(UserReactTheReview userReactTheReview) {
        ReactResponse userReactTheReviewResponse=null;
        Optional<ReviewReaction> optionalReviewReaction=reviewReactionRepo.findByUser_IdAndReview_Id(userReactTheReview.getUserId(),userReactTheReview.getReviewId());
        if(optionalReviewReaction.isPresent()){
            ReviewReaction reviewReaction = optionalReviewReaction.get();
            if(reviewReaction.getReactionType().equals(userReactTheReview.getReactionType())){
                reviewReactionRepo.delete(reviewReaction);
                userReactTheReviewResponse=ReactResponse.builder()
                        .isReacted(false)
                        .reactionType(null)
                        .build();

            }else {
                reviewReaction.setReactionType(userReactTheReview.getReactionType());
                reviewReactionRepo.save(reviewReaction);
                userReactTheReviewResponse=ReactResponse.builder()
                        .isReacted(true)
                        .reactionType(reviewReaction.getReactionType())
                        .build();
            }


        }else {
            ReviewReaction reviewReaction1 = reviewReactionRepo.save(ReviewReaction.builder()
                    .reactionType(userReactTheReview.getReactionType())
                    .review(Review.builder().id(userReactTheReview.getReviewId()).build())
                    .user(AccountModel.builder().id(userReactTheReview.getUserId()).build())
                    .build());
            userReactTheReviewResponse=ReactResponse.builder()
                    .isReacted(true)
                    .reactionType(reviewReaction1.getReactionType())
                    .build();
        }
        userReactTheReviewResponse.setLikes(reviewReactionRepo.countLikesForReview(userReactTheReview.getReviewId()));
        userReactTheReviewResponse.setDislikes(reviewReactionRepo.countDislikesForReview(userReactTheReview.getReviewId()));
        return userReactTheReviewResponse;
    }
}
