package com.greamz.backend.repository;

import com.greamz.backend.model.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGameRepo extends JpaRepository<GameModel, Long> {

//    List<GameModel> findBy(Long categoryId);
}
