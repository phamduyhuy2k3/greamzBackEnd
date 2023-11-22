package com.greamz.backend.service;

import com.greamz.backend.dto.GameLibrary;
import com.greamz.backend.dto.OrderDTO;
import com.greamz.backend.model.GameModel;
import com.greamz.backend.model.Orders;
import com.greamz.backend.model.OrdersDetail;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.repository.IOrderDetail;
import com.greamz.backend.repository.IOrderRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final IOrderRepo orderRepo;
    private final IOrderDetail orderDetailRepo;
    private final IGameRepo gameRepo;
    private final GameModelService gameModelService;
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional(readOnly = true)
    public Page<Orders> getAllOrdersByAccountId(Integer accountId, int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAllByAccount_Id(accountId,PageRequest.of(page, size, Sort.by("createdAt").descending()));
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
        });
        return ordersPage;
    }
    @Transactional(readOnly = true)
    public Page<Orders> getAllOrdersByOrdersStatus(String ordersStatus,Integer accountId, int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAllByOrdersStatusAndAccount_Id(OrdersStatus.valueOf(ordersStatus),accountId,PageRequest.of(page, size, Sort.by("createdAt").descending()));
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
        });
        return ordersPage;
    }
    @Transactional(readOnly = false)
    public Page<GameLibrary> getGamesThatUserBought(Integer accountId, Pageable pageable) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GameLibraryOfUser");
        query.registerStoredProcedureParameter("accountID", Integer.class, ParameterMode.IN);
        query.registerStoredProcedureParameter("orderStatus", String.class, ParameterMode.IN);
        query.setParameter("accountID", accountId);
        query.setParameter("orderStatus", "FAILED");
        query.execute();

        // Lấy danh sách kết quả từ stored procedure
        List<Object[]> resultList = query.getResultList();

        // Ánh xạ kết quả vào đối tượng GameLibrary
        List<GameLibrary> games = resultList.stream()
                .map(this::mapToGameLibrary)
                .collect(Collectors.toList());

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<GameLibrary> paginatedGames;
        if (games.size() < startItem) {
            paginatedGames = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, games.size());
            paginatedGames = games.subList(startItem, toIndex);
        }

        return new PageImpl<>(paginatedGames, PageRequest.of(currentPage, pageSize), games.size());
    }

    private GameLibrary mapToGameLibrary(Object[] row) {
        GameLibrary gameLibrary = new GameLibrary();
        gameLibrary.setAppid((Long) row[0]);
        gameLibrary.setName((String) row[2]);
        gameLibrary.setHeader_image((String) row[1]);
        gameLibrary.setTotalQuantity((BigDecimal) row[3]); // Suppose totalQuantity is at index 1 in Object[]


        return gameLibrary;
    }
    @Transactional
    public UUID saveOrder(Orders orders) {
        Orders orders1= orderRepo.saveAndFlush(orders);
        return orders1.getId();
    }
    @Transactional
    public Orders saveOrdersAndUpdateTheStockForGames(Orders orders){
        Orders orders1= orderRepo.saveAndFlush(orders);
        Set<OrdersDetail> ordersDetails = this.findOrdersDetailsByOrderId(orders.getId());
        gameModelService.updateStockForGameFromOrder(ordersDetails.stream().toList());
        return orders1;
    }
    @Transactional(readOnly = true)
    public Orders getOrdersById(UUID orderId){
        Orders orders=orderRepo.findById(orderId).orElseThrow();
        Hibernate.initialize(orders.getOrdersDetails());
        orders.getOrdersDetails().forEach(ordersDetail -> {
            Hibernate.initialize(ordersDetail.getGame());
            ordersDetail.getGame().setPlatform(null);
            ordersDetail.getGame().setSupported_languages(null);
            ordersDetail.getGame().setReviews(null);
            ordersDetail.getGame().setCategories(null);
            ordersDetail.getGame().setMovies(null);
            ordersDetail.getGame().setImages(null);
        });
        return orders;
    }
    @Transactional(readOnly = true)
    public Set<OrdersDetail> findOrdersDetailsByOrderId(UUID orderId){
        Set<OrdersDetail> ordersDetails = orderDetailRepo.findAllByOrders_Id(orderId);
        ordersDetails.forEach(ordersDetail -> {
            ordersDetail.setOrders(null);
            Hibernate.initialize(ordersDetail.getGame());
            ordersDetail.getGame().setPlatform(null);
            ordersDetail.getGame().setSupported_languages(null);
            ordersDetail.getGame().setReviews(null);
            ordersDetail.getGame().setCategories(null);
            ordersDetail.getGame().setMovies(null);
            ordersDetail.getGame().setImages(null);

        });
        return ordersDetails;
    }
    @Transactional(readOnly = true)
    public Map<String,Object> getAllOrdersDetailByOrderId(UUID orderId) {
        Set<OrdersDetail> ordersDetailPage = orderDetailRepo.findAllByOrders_Id(orderId);
        ordersDetailPage.forEach(ordersDetail -> {
            Hibernate.initialize(ordersDetail.getGame());
            ordersDetail.getGame().setPlatform(null);
            ordersDetail.getGame().setSupported_languages(null);
            ordersDetail.getGame().setReviews(null);
            ordersDetail.getGame().setCategories(null);
            ordersDetail.getGame().setMovies(null);
            ordersDetail.getGame().setImages(null);

            ordersDetail.setOrders(null);
        });
        Orders orders = orderRepo.findById(orderId).orElseThrow();
        OrderDTO orderDTO = OrderDTO.builder()
                .id(orders.getId())
                .createdAt(orders.getCreatedAt())
                .ordersStatus(orders.getOrdersStatus())
                .paymentmethod(orders.getPaymentmethod())
                .totalPrice(orders.getTotalPrice())
                .build();
        Map<String,Object> map = Map.of("order",orderDTO,"orderDetail",ordersDetailPage);
        return map;
    }
}
