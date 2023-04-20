package com.sampleapp.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class CreateItemRequestDTO {

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name cannot be null")
    @Size(min = 2, max = 30, message = "Name must be of 2 - 30 characters")
    private final String name;

    private final int quantity;

    @NotNull(message = "Price cannot be null")
    private final double price;
}
