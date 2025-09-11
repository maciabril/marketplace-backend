package com.uade.circulo.service;

import com.uade.circulo.entity.Order;
import com.uade.circulo.entity.OrderItem;
import com.uade.circulo.entity.exceptions.OutOfStockException;
import com.uade.circulo.enums.Status;
import com.uade.circulo.entity.Item;
import com.uade.circulo.repository.ItemRepository;
import com.uade.circulo.repository.OrderRepository;

import jakarta.transaction.Transactional;

//import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order createOrder(Order order) {
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