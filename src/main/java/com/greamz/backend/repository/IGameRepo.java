package com.greamz.backend.repository;

import com.greamz.backend.model.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameRepo extends JpaRepository<GameModel, Long> {

}
