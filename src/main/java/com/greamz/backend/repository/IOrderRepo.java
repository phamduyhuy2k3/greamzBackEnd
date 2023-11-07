package com.greamz.backend.repository;

import com.greamz.backend.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IOrderRepo extends JpaRepository<Orders, UUID> {
}
