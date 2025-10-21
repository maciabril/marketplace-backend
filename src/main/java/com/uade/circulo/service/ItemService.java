package com.uade.circulo.service;

import com.uade.circulo.entity.Item;
import com.uade.circulo.entity.dtos.ItemDto;
import com.uade.circulo.entity.dtos.ItemUpdateDto;
import com.uade.circulo.entity.exceptions.ItemNotFoundException;
import com.uade.circulo.enums.Category;
import com.uade.circulo.enums.Status;
import com.uade.circulo.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // NUEVO MÉTODO: Búsqueda con filtros y paginación
    public Page<Item> getItemsWithFilters(String name,
                                          Category category,
                                          Double minPrice,
                                          Double maxPrice,
                                          int page,
                                          int size,
                                          String sortBy,
                                          String sortDirection) {

        // Configurar ordenamiento
        Sort sort = sortDirection.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // Crear objeto Pageable (página, tamaño, ordenamiento)
        Pageable pageable = PageRequest.of(page, size, sort);

        // Llamar al repositorio con filtros
        return itemRepository.findByFilters(
                name,
                category,
                minPrice,
                maxPrice,
                Status.PUBLISHED,
                pageable
        );
    }

    // MÉTODOS EXISTENTES (no los toques)

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item createItem(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setPrice(itemDto.getPrice());
        item.setStatus(itemDto.getStatus());
        item.setStock(itemDto.getStock());
        item.setDiscount(itemDto.getDiscount());
        item.setCategory(itemDto.getCategory());
        
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, ItemUpdateDto itemUpdateDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));

        if (itemUpdateDto.getName() != null) item.setName(itemUpdateDto.getName());
        if (itemUpdateDto.getDescription() != null) item.setDescription(itemUpdateDto.getDescription());
        if (itemUpdateDto.getPrice() != null) item.setPrice(itemUpdateDto.getPrice());
        if (itemUpdateDto.getStatus() != null) item.setStatus(itemUpdateDto.getStatus());
        if (itemUpdateDto.getStock() != null) item.setStock(itemUpdateDto.getStock());
        if (itemUpdateDto.getDiscount() != null) item.setDiscount(itemUpdateDto.getDiscount());
        if (itemUpdateDto.getCategory() != null) item.setCategory(itemUpdateDto.getCategory());

        return itemRepository.save(item);
    }

    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException(id);
        }
        itemRepository.deleteById(id);
    }

    public List<Item> getItemsByPriceRange(Double minPrice, Double maxPrice) {
        return itemRepository.findByPriceBetween(minPrice, maxPrice);
    }
}
