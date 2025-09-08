package com.uade.circulo.service;

import com.uade.circulo.entity.Dto.ItemUpdateDto;
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

    public void updateItem(Long id, ItemUpdateDto itemUpdateDto) {

        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        
        item.setName( itemUpdateDto.getName()!= null ? itemUpdateDto.getName() : item.getName());
        item.setDescription(itemUpdateDto.getDescription() != null ?itemUpdateDto.getDescription() : item.getDescription());

        itemRepository.save(item);

    }

    public boolean deleteItem(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    itemRepository.delete(item);
                    return true;   // se eliminó
                })
                .orElse(false);    // no existía
    }


    public List<Item> filterByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("El precio mínimo no puede ser mayor que el máximo");
        }

        return itemRepository.findByPriceBetween(minPrice, maxPrice);
    }


}
