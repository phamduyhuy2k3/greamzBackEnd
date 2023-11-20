package com.greamz.backend.service;

import com.greamz.backend.dto.*;
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
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Page<Orders> getAllOrdersByAccountId(Integer accountId, int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAllByAccount_Id(accountId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
        });
        return ordersPage;
    }

    @Transactional(readOnly = true)
    public Page<Orders> getAllOrdersByOrdersStatus(String ordersStatus, Integer accountId, int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAllByOrdersStatusAndAccount_Id(OrdersStatus.valueOf(ordersStatus), accountId, PageRequest.of(page, size, Sort.by("createdAt").descending()));
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
        Orders orders1 = orderRepo.saveAndFlush(orders);
        return orders1.getId();
    }

    @Transactional
    public Orders saveOrdersAndUpdateTheStockForGames(Orders orders) {
        Orders orders1 = orderRepo.saveAndFlush(orders);
        Set<OrdersDetail> ordersDetailSet = (Set<OrdersDetail>) getAllOrdersDetailByOrderId(orders1.getId()).get("orderDetail");
        ordersDetailSet.forEach(ordersDetail -> {
            ordersDetail.getGame().setStock(ordersDetail.getGame().getStock() - ordersDetail.getQuantity());
        });
        gameRepo.saveAll(orders1.getOrdersDetails().stream().map(OrdersDetail::getGame).toList());
        return orders1;
    }

    @Transactional(readOnly = true)
    public Orders getOrdersById(UUID orderId) {
        return orderRepo.findById(orderId).orElseThrow();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAllOrdersDetailByOrderId(UUID orderId) {
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
        Map<String, Object> map = Map.of("order", orderDTO, "orderDetail", ordersDetailPage);
        return map;
    }

    @Transactional(readOnly = true)
    public List<Orders> findAll() {
        List<Orders> orders = orderRepo.findAll();
        orders.forEach(orders1 -> {
            orders1.setAccount(null);
            orders1.setOrdersDetails(null);
            orders1.setVoucher(null);
        });
        return orders;
    }

    @Transactional(readOnly = true)
    public Orders findById(UUID id) {
        Orders orders = orderRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found game category with id: " + id));
        Hibernate.initialize(orders.getAccount());
        orders.setOrdersDetails(null);
        orders.setVoucher(null);
        return orders;
    }

    //    @Transactional(readOnly = false)
//    public Orders save(Orders order) {
//        return orderRepo.save(order);
//    }
    @Transactional
    public void delete(UUID id) {
        Orders orders = orderRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found order with id: " + id));
        orderRepo.delete(orders);
    }

    @Transactional(readOnly = true)
    public Page<Orders> findAllPagination(Pageable pageable) {
        Page<Orders> ordersPage = orderRepo.findAll(pageable);
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
            orders.setVoucher(null);
        });
        return ordersPage;
    }

    @Transactional(readOnly = true)
    public List<OrderDetailsDTO> findOrderDetailsById(UUID id) {
        List<OrdersDetail> ordersDetails = orderDetailRepo.findAllByOrdersId(id);
        List<OrderDetailsDTO> orderDetailsDTOList = ordersDetails.stream()
                .map(ordersDetail -> {
                    Hibernate.initialize(ordersDetail.getGame());
                    Hibernate.initialize(ordersDetail.getOrders().getAccount());
//                    Hibernate.initialize(ordersDetail.getOrders().getVoucher());
                    Hibernate.initialize(ordersDetail.getOrders());
                    OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
                    orderDetailsDTO.setGame(
                            GameBasicDTO.builder()
                                    .name(ordersDetail.getGame().getName())
                                    .build()
                    );
                    orderDetailsDTO.setAccount(
                            AccountBasicDTO.builder()
                                    .username(ordersDetail.getOrders().getAccount().getUsername())
                                    .build()
                    );
//                    orderDetailsDTO.setVoucher(
//                            VoucherOrderDTO.builder()
//                                    .name(ordersDetail.getOrders().getVoucher().getName())
//                                    .build()
//                    );
                    orderDetailsDTO.setQuantity(ordersDetail.getQuantity());
                    orderDetailsDTO.setOrder(
                            OrderDTO.builder()
                                    .id(ordersDetail.getOrders().getId())
                                    .createdAt(ordersDetail.getOrders().getCreatedAt())
                                    .paymentmethod(ordersDetail.getOrders().getPaymentmethod())
                                    .totalPrice(ordersDetail.getOrders().getTotalPrice())
                                    .ordersStatus(ordersDetail.getOrders().getOrdersStatus())
                                    .build()
                    );
                    return orderDetailsDTO;
                }).toList();
        return orderDetailsDTOList;
    }
}
