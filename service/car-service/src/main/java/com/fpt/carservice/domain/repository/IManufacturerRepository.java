package com.fpt.carservice.domain.repository;

import com.fpt.carservice.domain.model.Manufacturer;

import java.util.Optional;

public interface IManufacturerRepository {
    
    Optional<Manufacturer> findById(Long id);
    
}