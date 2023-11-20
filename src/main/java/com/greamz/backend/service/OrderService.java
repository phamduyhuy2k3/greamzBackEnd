package com.greamz.backend.service;

import com.greamz.backend.model.Orders;
import com.greamz.backend.repository.IOrderRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final IOrderRepo repo;

    @Transactional
    public List<Orders> findAll() {
        List<Orders> orders = repo.findAll();
        return orders;
    }

    @Transactional
    public Orders saveOrder(Orders order) {
        return repo.save(order);
    }

    @Transactional
    public Orders findById(UUID id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Not found Order with id: " + id));
    }

    @Transactional
    public void deleteById(UUID id) {
        repo.deleteById(id);
    }

    @Transactional
    public Page<Orders> findAllPagination(Pageable pageable) {
        Page<Orders> orderPage = repo.findAll(pageable);
        return orderPage;
    }
}
