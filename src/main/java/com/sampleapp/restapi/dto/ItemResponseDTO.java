package com.sampleapp.restapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class ItemResponseDTO {

    private final Long id;

    private final String name;

    private final double price;
}
