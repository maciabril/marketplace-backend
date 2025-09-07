package com.uade.circulo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    //TO-DO: preguntar si está bien como public, o si debe ser privado.
    public enum OrderStatus {  
        PENDIENTE,
        PROCESANDO,
        COMPLETADO,
        CANCELADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "importe_total", nullable = false)
    private float importeTotal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public float getImporteTotal(){
        return importeTotal;
    }

    public void setImporteTotal(float importeTotal){
        this.importeTotal = importeTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return user != null ? user.getId() : null;
    }

    public void setUserId(User user) {
        this.user = user;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
