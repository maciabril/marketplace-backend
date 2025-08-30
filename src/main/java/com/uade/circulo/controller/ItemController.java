package com.uade.circulo.controller;

import com.uade.circulo.entity.Item;
import com.uade.circulo.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/catalog/products")
    public ResponseEntity<List<Item>> getAllProducts(
            @RequestParam(required = false) String filter) {
        List<Item> items = itemService.getAllProducts(filter);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/catalog/products/{id}")
    public ResponseEntity<Item> getProductById(@PathVariable Long id) {
        Item item = itemService.getProductById(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping("/products")
    public ResponseEntity<Item> createProduct(@RequestBody Item item) {
        Item createdItem = itemService.createProduct(item);
        return ResponseEntity.status(201).body(createdItem);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Item> updateProduct(@PathVariable Long id, @RequestBody Item item) {
        Item updatedItem = itemService.updateProduct(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = itemService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}