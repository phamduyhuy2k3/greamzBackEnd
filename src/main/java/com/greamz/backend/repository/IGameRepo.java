package com.greamz.backend.repository;

import com.greamz.backend.model.GameModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface IGameRepo extends JpaRepository<GameModel, Long>, JpaSpecificationExecutor<GameModel> {
    @Procedure(name = "GetTopSellingProductsInMonthYear", outputParameterName = "result")
    List<Object[]> getTopSellingProductsInMonthYear(
            @Param("yearParam") int yearParam,
            @Param("monthParam") int monthParam
    );
    @Query("SELECT COUNT(g) FROM GameModel g WHERE g.createdAt >= CURRENT_DATE - 7 AND g.createdAt < CURRENT_DATE")
    Long countGamesAddedLastWeek();

    @Query("SELECT COUNT(g) FROM GameModel g")
    Long countTotalGames();

    List<GameModel> findAllByCategoriesId(Long categoryId);
    long countAllByCategoriesId(Long categoryId);

    @Query("""
            select distinct g from GameModel g
            where g.appid in ?1
            """)
    List<GameModel> findAllByOrdersDetail(List<Long> ordersDetailId);

    @Override
    @EntityGraph(attributePaths = {"categories"})
    Optional<GameModel> findById(Long aLong);

    @Query("""
            select distinct g from GameModel g
            left join fetch g.categories c
            where cast(g.appid as string ) like %?1% or g.name like %?1% or cast(g.price as string ) like %?1%
            or c.name like %?1% or cast(c.categoryTypes as string) like %?1%
            """)
    Page<GameModel> searchGame(String search, Pageable pageable);

    @Query("""
            select distinct g from GameModel g
            left join fetch g.categories c
            where cast(g.appid as string ) like %?1% or g.name like %?1%
            """)
    Page<GameModel> searchGameByName(String search, Pageable pageable);

    @Query("""
            select distinct g from GameModel g
            left join fetch g.categories c
            where cast(c.categoryTypes as string) like %?1% or c.name like %?1%
            """)
    Page<GameModel> searchGameByCategory(String search, Pageable pageable);


    @Query(value = """
            select distinct g.* from game_model g
                          left join  game_category gc on  gc.game_id = g.appid
                          left join category c on c.id = gc.category_id
                          left join code_active ca on ca.game_appid = g.appid
            where  c.id in ?1 and ca.platform_id in ?2
            group by g.appid
            """, nativeQuery = true)
    List<GameModel> gameSimilar(List<Long> categoryId, List<Long> platformId);
//    @Query("""
//            select distinct g from GameModel g
//            join fetch g.categories c
//            where c.id in :categoriesId and g.platform.id = :platformId and g.price >= :minPrice and g.price <= :maxPrice
//            """)
//    Page<GameModel> searchGameFilterByCategoriesIdsPlatformPrice(@Param("categoriesId ") List<Long> categoriesId, @Param("platformId") Long platformId, @Param("minPrice") Double minPrice,@Param("maxPrice") Double maxPrice, Pageable pageable);
//
//    Page<GameModel> findAllByCategoriesIdAndPlatformIdAndPriceBetween(Long categoryId, Long platformId, Double minPrice, Double maxPrice, Pageable pageable);
}
