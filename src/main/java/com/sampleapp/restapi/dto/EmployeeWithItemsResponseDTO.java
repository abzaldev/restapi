package com.sampleapp.restapi.dto;

import lombok.*;
import java.util.Set;

@Data
@Builder
@RequiredArgsConstructor
public class EmployeeWithItemsResponseDTO {

    private final EmployeeResponseDTO employee;

    private final Set<ItemWithQuantityResponseDTO> items;
}