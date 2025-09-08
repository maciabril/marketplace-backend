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
    public ResponseEntity<List<Item>> getAllProducts() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }


    @GetMapping("/catalog/products/{id}")
    public ResponseEntity<Item> getProductById(@PathVariable Long id) {
        return itemService.getItemById(id)
        .map(ResponseEntity::ok)        // devuelve un 200 ok además de lo pedido
        .orElseGet(() -> ResponseEntity.notFound().build()); // devuelve 404
    }

    @PostMapping("/products")
    public ResponseEntity<Item> createProduct(@RequestBody Item item) {
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.status(201).body(createdItem);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Item> updateProduct(@PathVariable Long id, @RequestBody Item item) {
        Item updatedItem = itemService.updateItem(id, item);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = itemService.deleteItem(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}