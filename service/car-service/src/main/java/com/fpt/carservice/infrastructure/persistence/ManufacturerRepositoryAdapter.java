package com.fpt.carservice.infrastructure.persistence;

import com.fpt.carservice.domain.model.Manufacturer;
import com.fpt.carservice.domain.repository.IManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ManufacturerRepositoryAdapter implements IManufacturerRepository {

    private final ManufacturerJpaRepository manufacturerJpaRepository;

    @Override
    public Optional<Manufacturer> findById(Long id) {
        return manufacturerJpaRepository.findById(id);
    }
}