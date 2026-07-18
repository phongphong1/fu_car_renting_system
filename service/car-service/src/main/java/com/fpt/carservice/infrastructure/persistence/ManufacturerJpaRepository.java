package com.fpt.carservice.infrastructure.persistence;

import com.fpt.carservice.domain.model.Manufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManufacturerJpaRepository extends JpaRepository<Manufacturer, Long> {
}