package com.uade.circulo.repository;

import com.uade.circulo.entity.Item;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByPriceBetween(double min, double max);
}