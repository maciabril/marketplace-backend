package com.uade.circulo.controller;

import com.uade.circulo.entity.Order;
import com.uade.circulo.enums.OrderStatus;
import com.uade.circulo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/action")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/orders")
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        
        Page<Order> orders = orderService.getAllOrders(page, size, sortBy, sortDirection);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }



    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @PutMapping("/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        
        String statusString = request.get("orderStatus");
        if (statusString == null || statusString.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo 'orderStatus' es obligatorio");
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(statusString.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Estado inválido. Valores permitidos: PENDIENTE, EN_PREPARACION, ENVIADO, ENTREGADO"
            );
        }

        Order updatedOrder = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(updatedOrder);
    }
}
