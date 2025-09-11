package com.uade.circulo.controller;

import com.uade.circulo.entity.dtos.ItemDto;
import com.uade.circulo.entity.dtos.ItemUpdateDto;
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
    public ResponseEntity<List<ItemDto>> getAllProducts() {
        List<ItemDto> items = itemService.getAllItems();
        return ResponseEntity.ok(items);
    }


    @GetMapping("/catalog/products/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long id) {
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/products")
    public ResponseEntity<Item> createProduct(@RequestBody Item item) {
        Item createdItem = itemService.createItem(item);
        return ResponseEntity.status(201).body(createdItem);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ItemUpdateDto itemUpdateDto) {
        try {
            itemService.updateItem(id, itemUpdateDto);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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