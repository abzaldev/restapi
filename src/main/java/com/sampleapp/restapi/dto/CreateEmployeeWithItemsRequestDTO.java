package com.sampleapp.restapi.dto;

import jakarta.validation.Valid;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateEmployeeWithItemsRequestDTO {

    @Valid
    private CreateEmployeeRequestDTO employee;

    @Valid
    private Set<CreateItemRequestDTO> items;


}
