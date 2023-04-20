package com.sampleapp.restapi.service;

import com.sampleapp.restapi.dto.CreateItemRequestDTO;
import com.sampleapp.restapi.dto.ItemResponseDTO;
import com.sampleapp.restapi.dto.UpdateItemRequestDTO;
import com.sampleapp.restapi.model.Item;
import com.sampleapp.restapi.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public ItemResponseDTO getItemById(Long itemId) {
        Item foundItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));
        return itemToItemDto(foundItem);
    }

    @Transactional
    public ItemResponseDTO createItem(CreateItemRequestDTO itemDto) {
        Item item = createItemRequestDtoToItem(itemDto);
        Item savedItem = itemRepository.save(item);
        return ItemResponseDTO.builder()
                .id(savedItem.getId())
                .name(savedItem.getName())
                .price(savedItem.getPrice())
                .build();
    }

    @Transactional
    public ItemResponseDTO updateItem(Long itemId, UpdateItemRequestDTO itemDto) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));
        existingItem.setName(itemDto.getName());
        existingItem.setPrice(itemDto.getPrice());
        Item updatedItem = itemRepository.save(existingItem);
        return itemToItemDto(updatedItem);

    }

    @Transactional
    public void deleteItem(Long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item not found with id: " + itemId));
        itemRepository.deleteById(itemId);
    }


    public ItemResponseDTO itemToItemDto(Item item) {
        return ItemResponseDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .build();
    }

    public Item createItemRequestDtoToItem(CreateItemRequestDTO itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setPrice(itemDto.getPrice());
        return item;
    }
}
