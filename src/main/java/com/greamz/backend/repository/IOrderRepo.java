package com.greamz.backend.repository;

import com.greamz.backend.model.Orders;
import com.greamz.backend.enumeration.OrdersStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IOrderRepo extends JpaRepository<Orders, UUID> {
    @Query("SELECT SUM(o.totalPrice) FROM Orders o WHERE o.createdOn = CURRENT_TIMESTAMP ")
    Double findRevenueForCurrentDay();

    Page<Orders> findAllByAccount_Id(Integer accountId, Pageable pageable);
    List<Orders> findAllByAccountId(Integer accountId);
    @Query("select o from Orders o where o.ordersStatus = ?1 and o.account.id = ?2")
    Page<Orders>  findAllByOrdersStatusAndAccount_Id(OrdersStatus ordersStatus, Integer accountId, Pageable pageable);
    @Query("select o from Orders o where o.ordersStatus = ?1 and o.account.id = ?2")
    Optional<Set<Orders>> ffindAllByOrdersStatusAndAccount_Id(OrdersStatus ordersStatus, Integer accountId);
}
