package com.fpt.carservice.application.dto;

import com.fpt.carservice.domain.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplierResponse {
    private Long id;
    private String supplierName;
    private String supplierDescription;
    private String supplierAddress;

    public static SupplierResponse fromDomain(Supplier supplier) {
        if (supplier == null) return null;
        return SupplierResponse.builder()
                .id(supplier.getId())
                .supplierName(supplier.getSupplierName())
                .supplierDescription(supplier.getSupplierDescription())
                .supplierAddress(supplier.getSupplierAddress())
                .build();
    }
}