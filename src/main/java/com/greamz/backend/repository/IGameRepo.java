package com.greamz.backend.repository;

import com.greamz.backend.model.GameModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IGameRepo extends JpaRepository<GameModel, Long>, JpaSpecificationExecutor<GameModel> {


    List<GameModel> findAllByCategoriesId(Long categoryId);

    @Override
    @EntityGraph(attributePaths = {"categories"})
    Optional<GameModel> findById(Long aLong);

    @Query("""
            select distinct g from GameModel g
            join fetch g.categories c
            where cast(g.appid as string ) like %?1% or g.name like %?1% or cast(g.price as string ) like %?1%
            or c.name like %?1% or cast(c.categoryTypes as string) like %?1%
            """)
    Page<GameModel> searchGame(String search, Pageable pageable);
    @Query("""
            select distinct g from GameModel g
            join fetch g.categories c
            where c.id in :categoriesId and g.platform.id = :platformId and g.price >= :minPrice and g.price <= :maxPrice
            """)
    Page<GameModel> searchGameFilterByCategoriesIdsPlatformPrice(@Param("categoriesId ") List<Long> categoriesId, @Param("platformId") Long platformId, @Param("minPrice") Double minPrice,@Param("maxPrice") Double maxPrice, Pageable pageable);

    Page<GameModel> findAllByCategoriesIdAndPlatformIdAndPriceBetween(Long categoryId, Long platformId, Double minPrice, Double maxPrice, Pageable pageable);
}
