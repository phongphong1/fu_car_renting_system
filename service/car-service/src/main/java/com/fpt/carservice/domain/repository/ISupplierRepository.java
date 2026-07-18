package com.fpt.carservice.domain.repository;

import com.fpt.carservice.domain.model.Supplier;

import java.util.Optional;

public interface ISupplierRepository {
    
    Optional<Supplier> findById(Long id);
    
}