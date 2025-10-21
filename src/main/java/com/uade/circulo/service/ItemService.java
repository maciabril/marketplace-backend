package com.uade.circulo.service;

import com.uade.circulo.entity.dtos.ItemDto;
import com.uade.circulo.entity.dtos.ItemUpdateDto;
import com.uade.circulo.entity.exceptions.ItemNotFoundException;
import com.uade.circulo.entity.Item;
import com.uade.circulo.repository.ItemRepository;


import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;
    
    public List<ItemDto> getAllItems() {
        List<ItemDto> items = itemRepository.findAll().stream()
                .filter(item -> item.getStock() > 0)
                .map(item -> {
                    ItemDto.ItemDtoBuilder builder = ItemDto.builder()
                            .id(item.getId())
                            .name(item.getName())
                            .description(item.getDescription())
                            .status(item.getStatus())
                            .stock(item.getStock())
                            .price(item.getPrice())
                            .category(item.getCategory())
                            .imageName(item.getImageName())
                            .imageData(item.getImageData());

                    if (item.getDiscount() !=  0) {
                        builder.discount(item.getDiscount());
                        builder.discountedPrice(item.getPrice() * (1 - item.getDiscount() / 100.0));
                    }

                    return builder.build();
                })
                .toList();

        if (items.isEmpty()) {
            throw new RuntimeException("No hay items disponibles");
        }

        return items;
    }



    public ItemDto getItemById(Long id) {
        return itemRepository.findById(id)
                .map(item -> {
                    ItemDto dto = new ItemDto();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setDescription(item.getDescription());
                    dto.setStatus(item.getStatus());
                    dto.setStock(item.getStock());
                    dto.setPrice(item.getPrice());
                    dto.setCategory(item.getCategory());
                    dto.setImageName(item.getImageName());
                    dto.setImageData(item.getImageData());

                    if (item.getDiscount() !=  0) {
                        dto.setDiscount(item.getDiscount());
                        dto.setDiscountedPrice(item.getPrice() * (1 - item.getDiscount() / 100.0));
                    }

                    return dto;
                })
                .orElseThrow(() -> new ItemNotFoundException(id));
    }


    public Item createItem(Item item, MultipartFile file) throws java.io.IOException{
        item.setImageName(file.getOriginalFilename());
        item.setImageData(file.getBytes());
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
        if (updateDto.getCategory() != null) {
            item.setCategory(updateDto.getCategory());
        }

        if (updateDto.getPrice() != null) {
            item.setPrice(updateDto.getPrice());
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
