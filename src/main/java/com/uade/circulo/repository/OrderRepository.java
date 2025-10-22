package com.uade.circulo.repository;

import com.uade.circulo.entity.Order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser_Id(Long user_id);
    Page<Order> findByUserId(Long userId, Pageable pageable);
}
