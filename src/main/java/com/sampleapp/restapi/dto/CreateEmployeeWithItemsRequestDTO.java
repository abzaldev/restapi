package com.sampleapp.restapi.dto;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
