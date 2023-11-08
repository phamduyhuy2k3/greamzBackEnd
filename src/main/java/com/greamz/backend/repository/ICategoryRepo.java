package com.greamz.backend.repository;

import com.greamz.backend.enumeration.CategoryTypes;
import com.greamz.backend.model.Category;
import com.greamz.backend.model.GameModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ICategoryRepo extends JpaRepository<Category, Long> {

    Set<Category> findAllByCategoryTypes(CategoryTypes categoryTypes);

    List<Category> findAllByGameModelsAppid(Long gameId);
    @Query("""
            select distinct c from Category c
             where cast(c.id as string ) like %?1% or c.name like %?1% or cast(c.categoryTypes as string ) like %?1%
            
            """)
    Page<Category> searchCategory(String search, Pageable pageable);

}
