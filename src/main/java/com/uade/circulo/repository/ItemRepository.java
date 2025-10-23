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
    
    List<Item> findByPriceBetween(Double minPrice, Double maxPrice);
    
    Page<Item> findByNameContainingIgnoreCaseAndStatus(String name, Status status, Pageable pageable);
    Page<Item> findByCategoryAndStatus(Category category, Status status, Pageable pageable);
    Page<Item> findByPriceBetweenAndStatus(Double minPrice, Double maxPrice, Status status, Pageable pageable);
    Page<Item> findByCategoryAndPriceBetweenAndStatus(Category category, Double minPrice, Double maxPrice, Status status, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE " +
           "(:name IS NULL OR LOWER(i.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:category IS NULL OR i.category = :category) AND " +
           "(:minPrice IS NULL OR i.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR i.price <= :maxPrice) AND " +
           "(:hasDiscount IS NULL OR (:hasDiscount = true AND i.discount > 0) OR (:hasDiscount = false)) AND " +
           "(:inStock IS NULL OR (:inStock = true AND i.stock > 0) OR (:inStock = false)) AND " +
           "i.status = :status " +
           "ORDER BY " +
           "CASE WHEN :sortBy = 'price' AND :sortDirection = 'asc' THEN (i.price * (1 - i.discount / 100.0)) END ASC, " +
           "CASE WHEN :sortBy = 'price' AND :sortDirection = 'desc' THEN (i.price * (1 - i.discount / 100.0)) END DESC, " +
           "CASE WHEN :sortBy = 'name' AND :sortDirection = 'asc' THEN i.name END ASC, " +
           "CASE WHEN :sortBy = 'name' AND :sortDirection = 'desc' THEN i.name END DESC, " +
           "CASE WHEN :sortBy = 'id' AND :sortDirection = 'asc' THEN i.id END ASC, " +
           "CASE WHEN :sortBy = 'id' AND :sortDirection = 'desc' THEN i.id END DESC")
    Page<Item> findByFilters(@Param("name") String name,
                             @Param("category") Category category,
                             @Param("minPrice") Double minPrice,
                             @Param("maxPrice") Double maxPrice,
                             @Param("hasDiscount") Boolean hasDiscount,
                             @Param("inStock") Boolean inStock,
                             @Param("status") Status status,
                             @Param("sortBy") String sortBy,
                             @Param("sortDirection") String sortDirection,
                             Pageable pageable);
    
    Page<Item> findByStatus(Status status, Pageable pageable);
}