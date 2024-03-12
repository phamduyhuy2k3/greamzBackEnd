package com.greamz.backend.repository;

import com.greamz.backend.dto.dashboard.RevenueDTO;
import com.greamz.backend.dto.dashboard.TopSellingProductDTO;
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

@Repository
public interface IGameRepo extends JpaRepository<GameModel, Long>, JpaSpecificationExecutor<GameModel> {
    @Query("""
                        select distinct new 
                        com.greamz.backend.dto.dashboard.TopSellingProductDTO(
                            g.appid,
                            g.name,
                            g.header_image,
                            g.website,
                            g.discount,
                            g.price,
                            sum(od.quantity)
                        )
                         
                         from GameModel g
                        
                        join fetch OrdersDetail od on od.game.appid = g.appid
                        join fetch Orders o on o.id = od.orders.id
                        
                        where year(o.createdOn) = :yearParam and month(o.createdOn) = :monthParam and o.ordersStatus = 'SUCCESS'
                        group by g.appid
                        order by sum (od.quantity) desc

                        
            """)
    List<TopSellingProductDTO> getTopSellingProductsInMonthYear(
            @Param("yearParam") int yearParam,
            @Param("monthParam") int monthParam
    );
    @Query("""
                        select distinct g from GameModel g
                        join fetch OrdersDetail od on od.game.appid = g.appid
                        join fetch Orders o on o.id = od.orders.id
                        where year(o.createdOn) = :yearParam and month(o.createdOn) = :monthParam and o.ordersStatus = 'SUCCESS'
                        group by g.appid
                        order by sum (od.quantity) desc
            """)
    List<GameModel> getTopSellingProductsInMonthYearFromClient(@Param("yearParam") int yearParam,
                                                     @Param("monthParam") int monthParam);
    @Query("""
    select distinct new com.greamz.backend.dto.dashboard.RevenueDTO(
         case month(o.createdOn)
                    when 1 then 'Tháng 1'
                    when 2 then 'Tháng 2'
                    when 3 then 'Tháng 3'
                    when 4 then 'Tháng 4'
                    when 5 then 'Tháng 5'
                    when 6 then 'Tháng 6'
                    when 7 then 'Tháng 7'
                    when 8 then 'Tháng 8'
                    when 9 then 'Tháng 9'
                    when 10 then 'Tháng 10'
                    when 11 then 'Tháng 11'
                    when 12 then 'Tháng 12'
                end
        ,
        sum(o.totalPrice) 
    )
    from GameModel g
    join fetch OrdersDetail od on od.game.appid = g.appid
    join fetch Orders o on o.id = od.orders.id
    where year(o.createdOn) = :p_year and o.ordersStatus = 'SUCCESS' 
    group by month(o.createdOn)
    order by month(o.createdOn)
   
""")
    List<RevenueDTO> getRevenueByMonth(
            @Param("p_year") Integer year
    );


    @Query("SELECT COUNT(g) FROM GameModel g WHERE g.createdOn >= current_timestamp - 7 AND g.createdOn < current_timestamp")
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

    @Query("""
            select distinct g from GameModel g
                   
            order by g.discount desc
            limit 16
            """)
    List<GameModel> specialOffer();

    @Query("""
            select distinct g from GameModel g
            left join fetch g.codeActives ca
            where ca.platform.id in ?2 and g.appid in ?1
            """)
    List<GameModel> findAllByAppidsAndPlatformIds(List<Long> appids, List<Integer> platformIds);

//    @Query("""
//            select distinct g from GameModel g
//            join fetch g.categories c
//            where c.id in :categoriesId and g.platform.id = :platformId and g.price >= :minPrice and g.price <= :maxPrice
//            """)
//    Page<GameModel> searchGameFilterByCategoriesIdsPlatformPrice(@Param("categoriesId ") List<Long> categoriesId, @Param("platformId") Long platformId, @Param("minPrice") Double minPrice,@Param("maxPrice") Double maxPrice, Pageable pageable);
//
//    Page<GameModel> findAllByCategoriesIdAndPlatformIdAndPriceBetween(Long categoryId, Long platformId, Double minPrice, Double maxPrice, Pageable pageable);
}
