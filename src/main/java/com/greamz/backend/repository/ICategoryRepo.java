package com.greamz.backend.repository;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.GameCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ICategoryRepo extends JpaRepository<GameCategory,Long> {
    Set<GameCategory> findAllByCategoryTypes(CategoryTypes categoryTypes);
}
