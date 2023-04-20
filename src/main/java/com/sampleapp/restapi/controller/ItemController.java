package com.sampleapp.restapi.controller;

import com.sampleapp.restapi.dto.CreateItemRequestDTO;
import com.sampleapp.restapi.dto.ItemResponseDTO;
import com.sampleapp.restapi.dto.UpdateItemRequestDTO;
import com.sampleapp.restapi.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> getItemById(@PathVariable Long id) {
        ItemResponseDTO itemDto = itemService.getItemById(id);
        return ResponseEntity.ok(itemDto);
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(@Valid @RequestBody CreateItemRequestDTO itemDto) {
        ItemResponseDTO savedItemDto = itemService.createItem(itemDto);
        return ResponseEntity.created(URI.create("/items/" + savedItemDto.getId()))
                .body(savedItemDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> updateItem( @PathVariable Long id,
            @Valid @RequestBody UpdateItemRequestDTO itemDto) {
        ItemResponseDTO updatedItemDto = itemService.updateItem(id, itemDto);
        return ResponseEntity.ok(updatedItemDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
