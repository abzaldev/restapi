package com.sampleapp.restapi.dto;

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
