package com.sampleapp.restapi.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
public class EmployeeWithItemsResponseDTO {

    private final EmployeeResponseDTO employee;

    private final Set<ItemWithQuantityResponseDTO> items;
}