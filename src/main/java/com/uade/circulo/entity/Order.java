package com.uade.circulo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.uade.circulo.enums.PaymentMethod;

import com.uade.circulo.enums.OrderStatus;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "importe_total", nullable = false)
    private double importeTotal;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
    
    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false, length = 100)
    private String phone;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(length = 500)
    private String addressLine2;  // OPCIONAL (departamento, piso, etc.)

    @Column(nullable = false, length = 100)
    private String city;

    @Column(nullable = false, length = 20)
    private String postalCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
    }

    public double getImporteTotal(){
        return importeTotal;
    }

    public void setImporteTotal(double importeTotal){
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
