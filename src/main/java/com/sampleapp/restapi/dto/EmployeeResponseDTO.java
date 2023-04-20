package com.sampleapp.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Builder
public class EmployeeResponseDTO {

    private final Long id;

    private final String name;
}
