package com.fpt.carservice.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateCarRequest {

    @NotBlank(message = "Tên xe không được để trống!")
    private String carName;

    private String carDescription;

    @NotNull(message = "Số cửa không được để trống!")
    @Min(value = 2, message = "Xe ít nhất phải có 2 cửa!")
    private Integer numberOfDoors;

    @NotNull(message = "Số ghế không được để trống!")
    @Min(value = 2, message = "Xe ít nhất phải có 2 chỗ ngồi!")
    private Integer seatingCapacity;

    @NotBlank(message = "Loại nhiên liệu không được để trống!")
    private String fuelType;

    @NotNull(message = "Năm sản xuất không được để trống!")
    private Integer year;

    @NotNull(message = "Giá thuê theo ngày không được để trống!")
    @Positive(message = "Giá thuê phải lớn hơn 0!")
    private BigDecimal carRentingPricePerDay;

    @NotNull(message = "ID Hãng sản xuất không được để trống!")
    private Long manufacturerID;

    @NotNull(message = "ID Nhà cung cấp không được để trống!")
    private Long supplierID;
}