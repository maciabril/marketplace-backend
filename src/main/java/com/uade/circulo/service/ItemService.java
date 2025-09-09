package com.uade.circulo.service;

import com.uade.circulo.entity.Dto.ItemDto;
import com.uade.circulo.entity.Dto.ItemUpdateDto;
import com.uade.circulo.enums.Status;
import com.uade.circulo.entity.Item;
import com.uade.circulo.repository.ItemRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    
    //todo: modificar price para que sea  precio original y poner discountPrice, que tenga el descuento - ana
    public List<ItemDto> getAllItems() {
    return itemRepository.findAll().stream()
            .map(item -> ItemDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .status(item.getStatus())
                    .stock(item.getStock())
                    .discount(item.getDiscount())
                    .price(item.getPrice() * (1 - item.getDiscount() / 100.0)) // precio con descuento
                    .build())
            .toList();
    }

    //todo: modificar price para que sea  precio original y poner discountPrice, que tenga el descuento - ana
    public Optional<ItemDto> getItemById(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    ItemDto dto = new ItemDto();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setDescription(item.getDescription());
                    dto.setStatus(item.getStatus());
                    dto.setStock(item.getStock());
                    dto.setDiscount(item.getDiscount());
                    dto.setPrice(item.getPrice() * (1 - item.getDiscount() / 100.0)); //precio con descuento
                    return dto;
                });
    }


    public Item createItem(Item item) {
        return itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, ItemUpdateDto updateDto) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item no encontrado con ID: " + itemId));

        if (updateDto.getName() != null) {
            item.setName(updateDto.getName());
        }

        if (updateDto.getDescription() != null) {
            item.setDescription(updateDto.getDescription());
        }

        if (updateDto.getStatus() != null) {
            item.setStatus(updateDto.getStatus());
        }
        if (updateDto.getDiscount() != null) {
          int discount = updateDto.getDiscount();
            if (discount < 0 || discount > 100) {
                throw new IllegalArgumentException("El descuento debe estar entre 0 y 100");
            }
            item.setDiscount(discount);
        }

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
