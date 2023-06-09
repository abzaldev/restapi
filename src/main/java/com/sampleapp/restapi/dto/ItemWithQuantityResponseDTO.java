package com.sampleapp.restapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
public class ItemWithQuantityResponseDTO {

    private final Long id;

    private final String name;

    private final double price;

    private final int quantity;
}
