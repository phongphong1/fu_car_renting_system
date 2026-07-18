package com.fpt.carservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "manufacturer")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manufacturer_id", nullable = false)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Size(max = 100)
    @Column(name = "manufacturer_country", length = 100)
    private String manufacturerCountry;

    public Manufacturer(String manufacturerName, String manufacturerCountry) {
        if (manufacturerName == null || manufacturerName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà sản xuất không được để trống!");
        }
        this.manufacturerName = manufacturerName;
        this.manufacturerCountry = manufacturerCountry;
    }

    public void updateInfo(String newName, String newCountry) {
        if (newName != null && !newName.trim().isEmpty()) {
            this.manufacturerName = newName;
        }
        this.manufacturerCountry = newCountry;
    }

}