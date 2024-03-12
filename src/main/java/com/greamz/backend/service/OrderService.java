package com.greamz.backend.service;

import com.greamz.backend.dto.account.AccountBasicDTO;
import com.greamz.backend.dto.game.GameBasicDTO;
import com.greamz.backend.dto.game.GameLibrary;
import com.greamz.backend.dto.order.OrderDTO;
import com.greamz.backend.dto.order_detail.OrderDetailsDTO;
import com.greamz.backend.dto.platform.PlatformBasicDTO;
import com.greamz.backend.dto.review.ReviewBasic;
import com.greamz.backend.dto.voucher.VoucherOrderDTO;
import com.greamz.backend.model.Orders;
import com.greamz.backend.model.OrdersDetail;
import com.greamz.backend.enumeration.OrdersStatus;
import com.greamz.backend.model.Review;
import com.greamz.backend.repository.ICodeActiveRepo;
import com.greamz.backend.repository.IGameRepo;
import com.greamz.backend.repository.IOrderDetail;
import com.greamz.backend.repository.IOrderRepo;
import com.greamz.backend.repository.IReviewRepo;
import com.greamz.backend.util.Mapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
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
    private final ICodeActiveRepo codeActiveRepo;
    private final GameModelService gameModelService;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Double findRevenueForCurrentDay() {
        return orderRepo.findRevenueForCurrentDay();
    }

    @Transactional(readOnly = true)
    public Page<Orders> getAllOrdersByAccountId(Integer accountId, int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAllByAccount_Id(accountId, PageRequest.of(page, size, Sort.by("createdOn").descending()));
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
        });
        return ordersPage;
    }

    @Transactional(readOnly = true)
    public Page<Orders> getAllOrdersByOrdersStatus(String ordersStatus, Integer accountId, int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAllByOrdersStatusAndAccount_Id(OrdersStatus.valueOf(ordersStatus), accountId, PageRequest.of(page, size, Sort.by("createdOn").descending()));
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
        });
        return ordersPage;
    }

    @Transactional(readOnly = true)
    public Page<GameLibrary> getGamesThatUserBought(Integer accountId, Pageable pageable) {
        return codeActiveRepo.findGameLibraryByAccountId(accountId, pageable);
    }

//    private GameLibrary mapToGameLibrary(Object[] row) {
//        GameLibrary gameLibrary = new GameLibrary();
//        gameLibrary.setAppid((Long) row[0]);
//        gameLibrary.setName((String) row[2]);
//        gameLibrary.setHeader_image((String) row[1]);
//        gameLibrary.setTotalQuantity((BigDecimal) row[3]); // Suppose totalQuantity is at index 1 in Object[]
//
//
//        return gameLibrary;
//    }

    @Transactional
    public UUID saveOrder(Orders orders) {
        Orders orders1 = orderRepo.saveAndFlush(orders);
        return orders1.getId();
    }

    @Transactional

    public Orders saveOrdersAndUpdateTheStockForGames(Orders orders) {
        Orders orders1 = orderRepo.saveAndFlush(orders);
        Set<OrdersDetail> ordersDetails = this.findOrdersDetailsByOrderId(orders.getId());
        gameModelService.updateStockForGameFromOrder(ordersDetails.stream().toList());
        return orders1;
    }

    @Transactional(readOnly = true)

    public Orders getOrdersById(UUID orderId) {
        Orders orders = orderRepo.findById(orderId).orElseThrow();
        Hibernate.initialize(orders.getOrdersDetails());
        orders.getOrdersDetails().forEach(ordersDetail -> {
            Hibernate.initialize(ordersDetail.getGame());
            ordersDetail.getGame().setPlatforms(null);
            ordersDetail.getGame().setSupported_languages(null);
            ordersDetail.getGame().setReviews(null);
            ordersDetail.getGame().setCategories(null);
            ordersDetail.getGame().setMovies(null);
            ordersDetail.getGame().setImages(null);
        });
        return orders;
    }

    @Transactional(readOnly = true)
    public Set<OrdersDetail> findOrdersDetailsByOrderId(UUID orderId) {
        Set<OrdersDetail> ordersDetails = orderDetailRepo.findAllByOrders_Id(orderId);
        ordersDetails.forEach(ordersDetail -> {
            ordersDetail.setOrders(null);
            Hibernate.initialize(ordersDetail.getGame());
            ordersDetail.getGame().setPlatforms(null);
            ordersDetail.getGame().setSupported_languages(null);
            ordersDetail.getGame().setReviews(null);
            ordersDetail.getGame().setCategories(null);
            ordersDetail.getGame().setMovies(null);
            ordersDetail.getGame().setImages(null);

        });
        return ordersDetails;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getAllOrdersDetailByOrderId(UUID orderId) {

        Orders orders = orderRepo.findById(orderId).orElseThrow();
        Hibernate.initialize(orders.getOrdersDetails());
        OrderDTO orderDTO = OrderDTO.builder()
                .id(orders.getId())
                .createdOn(orders.getCreatedOn())
                .ordersStatus(orders.getOrdersStatus())
                .paymentmethod(orders.getPaymentmethod())
                .totalPrice(orders.getTotalPrice())
                .build();
        List<OrderDetailsDTO> orderDetailsDTOS = new ArrayList<>();
        orders.getOrdersDetails().forEach(ordersDetail -> {
            OrderDetailsDTO orderDetailsDTO = Mapper.mapObject(ordersDetail, OrderDetailsDTO.class);
            orderDetailsDTOS.add(orderDetailsDTO);
        });
        return Map.of("order", orderDTO, "orderDetail", orderDetailsDTOS);
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
    public Page<Orders> findAllPagination(int page, int size) {
        Page<Orders> ordersPage = orderRepo.findAll(PageRequest.of(page, size));
        ordersPage.forEach(orders -> {
            orders.setAccount(null);
            orders.setOrdersDetails(null);
            orders.setVoucher(null);
        });
        return ordersPage;
    }

    @Transactional(readOnly = true)
    public List<Orders> findAllOrdersByAccountId(Integer accountId) {
        List<Orders> orders = orderRepo.findAllByAccountId(accountId);
        orders.forEach(orders1 -> {
            Hibernate.initialize(orders1.getPaymentmethod());
            orders1.setAccount(null);
            orders1.setOrdersDetails(null);
            orders1.setVoucher(null);
        });
        return orders;
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
                                    .appid(ordersDetail.getGame().getAppid())
                                    .name(ordersDetail.getGame().getName())
                                    .header_image(ordersDetail.getGame().getHeader_image())
                                    .build()
                    );
                    orderDetailsDTO.setAccount(
                            AccountBasicDTO.builder()
                                    .username(ordersDetail.getOrders().getAccount().getUsername())
                                    .id(ordersDetail.getOrders().getAccount().getId())
                                    .photo(ordersDetail.getOrders().getAccount().getPhoto())
                                    .build()
                    );
//                    orderDetailsDTO.setVoucher(
//                            VoucherOrderDTO.builder()
//                                    .name(ordersDetail.getOrders().getVoucher().getName())
//                                    .build()
//                    );
                    orderDetailsDTO.setQuantity(ordersDetail.getQuantity());
                    orderDetailsDTO.setPrice(ordersDetail.getPrice());
                    orderDetailsDTO.setOrder(
                            OrderDTO.builder()
                                    .id(ordersDetail.getOrders().getId())
                                    .createdOn(ordersDetail.getOrders().getCreatedOn())
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
