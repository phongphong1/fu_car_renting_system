package com.fpt.carservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Table(name = "car_information")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "car_name", nullable = false)
    private String carName;

    @Column(name = "car_description", length = Integer.MAX_VALUE)
    private String carDescription;

    @Column(name = "number_of_doors")
    private Integer numberOfDoors;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @Size(max = 50)
    @Column(name = "fuel_type", length = 50)
    private String fuelType;

    @Column(name = "year")
    private Integer year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Size(max = 50)
    @Column(name = "car_status", length = 50)
    private String carStatus;

    @Column(name = "car_renting_price_per_day", precision = 12, scale = 2)
    private BigDecimal carRentingPricePerDay;


    public CarInformation(String carName, String carDescription, Integer numberOfDoors,
                          Integer seatingCapacity, String fuelType, Integer year,
                          Manufacturer manufacturer, Supplier supplier, BigDecimal carRentingPricePerDay) {

        if (carName == null || carName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe không được để trống!");
        }
        if (manufacturer == null || supplier == null) {
            throw new IllegalArgumentException("Xe phải thuộc về một Hãng và Nhà cung cấp hợp lệ!");
        }

        this.carName = carName;
        this.carDescription = carDescription;
        this.numberOfDoors = numberOfDoors;
        this.seatingCapacity = seatingCapacity;
        this.fuelType = fuelType;
        this.year = year;
        this.manufacturer = manufacturer;
        this.supplier = supplier;
        this.carRentingPricePerDay = carRentingPricePerDay;

        this.carStatus = "AVAILABLE";
    }

    public void changeStatus(String newStatus) {
        this.carStatus = newStatus;
    }

    public void updateRentalPrice(BigDecimal newPrice) {
        if (newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Giá thuê không hợp lệ!");
        }
        this.carRentingPricePerDay = newPrice;
    }

    public void updateBasicInformation(String carName, String carDescription, Integer numberOfDoors,
                                       Integer seatingCapacity, String fuelType, Integer year,
                                       Manufacturer manufacturer, Supplier supplier) {

        if (carName == null || carName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên xe không được để trống!");
        }
        if (manufacturer == null || supplier == null) {
            throw new IllegalArgumentException("Xe phải thuộc về một Hãng và Nhà cung cấp hợp lệ!");
        }

        this.carName = carName;
        this.carDescription = carDescription;
        this.numberOfDoors = numberOfDoors;
        this.seatingCapacity = seatingCapacity;
        this.fuelType = fuelType;
        this.year = year;
        this.manufacturer = manufacturer;
        this.supplier = supplier;
    }

}