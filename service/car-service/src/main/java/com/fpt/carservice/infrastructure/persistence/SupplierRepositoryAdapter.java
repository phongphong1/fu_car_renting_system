package com.fpt.carservice.infrastructure.persistence;

import com.fpt.carservice.domain.model.Supplier;
import com.fpt.carservice.domain.repository.ISupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SupplierRepositoryAdapter implements ISupplierRepository {

    private final SupplierJpaRepository supplierJpaRepository;

    @Override
    public Optional<Supplier> findById(Long id) {
        return supplierJpaRepository.findById(id);
    }

    @Override
    public List<Supplier> findAll() {
        return supplierJpaRepository.findAll();
    }
}