package com.greamz.backend.repository;

import com.greamz.backend.model.OrdersDetail;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface IOrderDetail extends JpaRepository<OrdersDetail, Long>{
    Set<OrdersDetail> findAllByOrders_Id(UUID orderId);

    List<OrdersDetail> findAllByOrdersId(UUID id);
}
