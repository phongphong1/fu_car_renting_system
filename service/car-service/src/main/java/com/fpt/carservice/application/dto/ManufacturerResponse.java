package com.fpt.carservice.application.dto;

import com.fpt.carservice.domain.model.Manufacturer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerResponse {
    private Long id;
    private String manufacturerName;
    private String description;
    private String manufacturerCountry;

    public static ManufacturerResponse fromDomain(Manufacturer manufacturer) {
        if (manufacturer == null) return null;
        return ManufacturerResponse.builder()
                .id(manufacturer.getId())
                .manufacturerName(manufacturer.getManufacturerName())
                .description(manufacturer.getDescription())
                .manufacturerCountry(manufacturer.getManufacturerCountry())
                .build();
    }
}