package com.fpt.carservice.infrastructure.persistence;

import com.fpt.carservice.domain.model.CarInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarJpaRepository extends JpaRepository<CarInformation, Long> {

    List<CarInformation> findByCarStatus(String status);
}