package com.greamz.backend.repository;

import com.greamz.backend.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findAllByGameAppid(Long appid);
    Page<Review> findAllByGameAppid(Long appid, Pageable pageable);
    List<Review> findAllByAccount_Id(Integer id);
    Integer countAllByGameAppid(Long appid);
    @Query("select avg(r.rating) from Review r where r.game.appid = ?1")
    Double calculateAverageRating(Long appid);


}
