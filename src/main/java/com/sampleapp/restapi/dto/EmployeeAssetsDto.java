package com.sampleapp.restapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class EmployeeAssetsDto {

    private final Long id;

    private final String name;

    private final int itemQuantity;

    private final double itemsTotalPrice;
}
