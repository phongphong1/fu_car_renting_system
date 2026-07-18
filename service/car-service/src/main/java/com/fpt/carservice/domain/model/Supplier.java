package com.fpt.carservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "supplier")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "supplier_name", nullable = false)
    private String supplierName;

    @Column(name = "supplier_description", length = Integer.MAX_VALUE)
    private String supplierDescription;

    @Size(max = 500)
    @Column(name = "supplier_address", length = 500)
    private String supplierAddress;

    public Supplier(String supplierName, String supplierDescription, String supplierAddress ) {
        if (supplierName == null || supplierName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống!");
        }
        this.supplierName = supplierName;
        this.supplierDescription = supplierDescription;
        this.supplierAddress = supplierAddress;
    }

}