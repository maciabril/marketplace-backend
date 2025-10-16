package com.uade.circulo.service;

import com.uade.circulo.entity.Order;
import com.uade.circulo.entity.OrderItem;
import com.uade.circulo.enums.Status;
import com.uade.circulo.entity.Item;
import com.uade.circulo.repository.ItemRepository;
import com.uade.circulo.repository.OrderRepository;

import jakarta.transaction.Transactional;

//import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uade.circulo.entity.User;
import com.uade.circulo.entity.exceptions.OrderAccessDeniedException;
import com.uade.circulo.entity.exceptions.OrderNotFoundException;
import com.uade.circulo.entity.exceptions.OutOfStockException;
import com.uade.circulo.enums.Role;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Order> getAllOrders() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Order> orders;

        if (currentUser.getRole() == Role.ADMIN) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByUser_Id(currentUser.getId());
        }

        if (orders.isEmpty()) {
            throw new OrderNotFoundException("No hay órdenes disponibles");
        }

        return orders;
    }


    public Order getOrderById(Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        if (currentUser.getRole() == Role.ADMIN) {
            return order; // si es admin puede ver cualquier orden
        }

        if (order.getUser() != null && order.getUser().getId().equals(currentUser.getId())) {
            return order; // un usuario puede ver las ordenes que él hizo
        }

        throw new OrderAccessDeniedException(id); // usuario no autorizado
    }

    @Transactional
    public Order createOrder(Order order) {
        
        // Validar usuario autenticado y que este no sea admin.
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser.getRole() == Role.ADMIN) {
            throw new OrderAccessDeniedException("Usuario vendedor no puede crear órdenes.");
        }

        //Validar que el usuario solo pueda crear órdenes a su nombre
        if (order.getUser() == null || !order.getUser().getId().equals(currentUser.getId())) {
            throw new OrderAccessDeniedException("No puede crear una orden para otro usuario.");
        }
        
        // Validar Stock
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("La orden debe contener al menos un producto");
        }

        double total = 0.0;

        // Verificar que cada item exista y esté en stock.
        for (OrderItem orderItem : order.getItems()) {
            Long itemId = orderItem.getItem().getId();
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + itemId));

            if (item.getStock() < orderItem.getCantidad()) {
                throw new OutOfStockException(item.getName());
            }

            //Se calcula el subtotal de la orden y se actualiza el subtotal.
            double precioUnitario = item.getPrice() * (1 - item.getDiscount() / 100.0); //con descuento
            double subtotal = precioUnitario * orderItem.getCantidad();
            orderItem.setPrecioUnitario(precioUnitario);
            orderItem.setSubtotal(subtotal);
            orderItem.setOrder(order);

            orderItem.setItem(item);

            item.setStock(item.getStock() - orderItem.getCantidad());
            if(item.getStock() == 0) {
                item.setStatus(Status.SOLD);;
            }
            itemRepository.save(item);

            total += subtotal;
        }

        //Pasar el total y actualizar el estado de la orden
        order.setOrderStatus(Order.OrderStatus.PENDIENTE);
        order.setImporteTotal(total); 

        // Guardar la orden en base.
        return orderRepository.save(order);
    }
}