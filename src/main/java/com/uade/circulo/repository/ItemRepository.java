package com.uade.circulo.repository;

import com.uade.circulo.entity.Item;
import com.uade.circulo.enums.Category;
import com.uade.circulo.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
    // Método existente
    List<Item> findByPriceBetween(Double minPrice, Double maxPrice);
    
    // NUEVOS MÉTODOS PARA FILTROS Y PAGINACIÓN
    
    // Búsqueda por nombre (contiene)
    Page<Item> findByNameContainingIgnoreCaseAndStatus(String name, Status status, Pageable pageable);
    
    // Búsqueda por categoría
    Page<Item> findByCategoryAndStatus(Category category, Status status, Pageable pageable);
    
    // Búsqueda por rango de precio
    Page<Item> findByPriceBetweenAndStatus(Double minPrice, Double maxPrice, Status status, Pageable pageable);
    
    // Búsqueda por categoría y rango de precio
    Page<Item> findByCategoryAndPriceBetweenAndStatus(Category category, Double minPrice, Double maxPrice, Status status, Pageable pageable);
    
    // Búsqueda combinada (nombre, categoría, precio) - Query personalizada
    @Query("SELECT i FROM Item i WHERE " +
           "(:name IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR i.price <= :maxPrice) AND " +
           "i.status = :status")
    Page<Item> findByFilters(@Param("name") String name,
                             @Param("category") Category category,
                             @Param("minPrice") Double minPrice,
                             @Param("maxPrice") Double maxPrice,
                             @Param("status") Status status,
                             Pageable pageable);
    
    // Todos los productos publicados con paginación
    Page<Item> findByStatus(Status status, Pageable pageable);
}