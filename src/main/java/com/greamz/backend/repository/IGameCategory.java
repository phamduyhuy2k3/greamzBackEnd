package com.greamz.backend.repository;

import com.greamz.backend.model.GameCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGameCategory extends JpaRepository<GameCategory, Long> {
}
