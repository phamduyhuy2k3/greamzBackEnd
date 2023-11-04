package com.greamz.backend.repository;

import com.greamz.backend.model.GameModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IGameRepo extends JpaRepository<GameModel, Long> {

//    List<GameModel> findBy(Long categoryId);

    @Override
    @Query("select distinct g from GameModel g join fetch g.categories")
    Page<GameModel> findAll(Pageable pageable);

    List<GameModel> findAllByCategoriesId(Long categoryId);

    @Override
    @EntityGraph(attributePaths = {"categories"})
    Optional<GameModel> findById(Long aLong);

    @Query("""
            select distinct g from GameModel g
             where cast(g.appid as string ) like %?1% or g.name like %?1% or cast(g.price as string ) like %?1%
            
            """)
    Page<GameModel> searchGame(String search, Pageable pageable);
}
