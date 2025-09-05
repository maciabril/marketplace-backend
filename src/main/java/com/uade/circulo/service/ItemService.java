package com.uade.circulo.service;

import com.uade.circulo.entity.Item;
import com.uade.circulo.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, Item itemDetails) {

        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setName(itemDetails.getName());
        item.setDescription(itemDetails.getDescription());
        item.setPrice(itemDetails.getPrice());
        item.setStatus(itemDetails.getStatus());

        return itemRepository.save(item);

    }

    public void deleteItem(Long id) {

        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        
        itemRepository.delete(item);

    }

    public List<Item> filterByPrice(double filter) {

        return itemRepository.findAll(); // Placeholder for actual filtering logic
    }

}
