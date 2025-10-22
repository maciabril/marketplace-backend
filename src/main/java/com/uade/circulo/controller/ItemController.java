package com.uade.circulo.controller;

import com.uade.circulo.entity.Item;
import com.uade.circulo.entity.dtos.ItemDto;
import com.uade.circulo.entity.dtos.ItemUpdateDto;
import com.uade.circulo.enums.Category;
import com.uade.circulo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/catalog/products/search")
    public ResponseEntity<Page<Item>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean hasDiscount,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Page<Item> items = itemService.getItemsWithFilters(
            name, 
            category, 
            minPrice, 
            maxPrice, 
            hasDiscount,
            inStock,
            page, 
            size, 
            sortBy, 
            sortDirection
        );
        
        return ResponseEntity.ok(items);
    }

    @GetMapping("/catalog/products")
    public ResponseEntity<List<Item>> getAllItems() {
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/catalog/products/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<Item> createItem(@RequestBody ItemDto itemDto) {
        Item createdItem = itemService.createItem(itemDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody ItemUpdateDto itemUpdateDto) {
        Item updatedItem = itemService.updateItem(id, itemUpdateDto);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}