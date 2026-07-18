package com.fpt.carservice.application.dto;

import com.fpt.carservice.domain.model.CarInformation;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CarResponse {
    private Long carID;
    private String carName;
    private String carDescription;
    private Integer numberOfDoors;
    private Integer seatingCapacity;
    private String fuelType;
    private Integer year;
    private String carStatus;
    private BigDecimal carRentingPricePerDay;
    private Long manufacturerID;
    private String manufacturerName;
    private Long supplierID;
    private String supplierName;

    public static CarResponse from(CarInformation car) {
        if (car == null) return null;
        return CarResponse.builder()
                .carID(car.getId())
                .carName(car.getCarName())
                .carDescription(car.getCarDescription())
                .numberOfDoors(car.getNumberOfDoors())
                .seatingCapacity(car.getSeatingCapacity())
                .fuelType(car.getFuelType())
                .year(car.getYear())
                .carStatus(car.getCarStatus())
                .carRentingPricePerDay(car.getCarRentingPricePerDay())
                .manufacturerID(car.getManufacturer().getId())
                .manufacturerName(car.getManufacturer().getManufacturerName())
                .supplierID(car.getSupplier().getId())
                .supplierName(car.getSupplier().getSupplierName())
                .build();
    }
}