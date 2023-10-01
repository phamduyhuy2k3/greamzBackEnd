package com.greamz.backend.repository;

import com.greamz.backend.model.GameModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGameRepo extends JpaRepository<GameModel, Long> {

}
