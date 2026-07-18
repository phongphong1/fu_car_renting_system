package com.fpt.carservice.domain.repository;

import com.fpt.carservice.domain.model.CarInformation;

import java.util.List;
import java.util.Optional;

public interface ICarRepository {

    CarInformation save(CarInformation car);

    Optional<CarInformation> findById(Long id);

    List<CarInformation> findAll();

    List<CarInformation> findByCarStatus(String status);

    void delete(CarInformation car);
}