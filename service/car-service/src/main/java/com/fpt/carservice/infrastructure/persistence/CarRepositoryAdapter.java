package com.fpt.carservice.infrastructure.persistence;

import com.fpt.carservice.domain.model.CarInformation;
import com.fpt.carservice.domain.repository.ICarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CarRepositoryAdapter implements ICarRepository {

    private final CarJpaRepository carJpaRepository;

    @Override
    public CarInformation save(CarInformation car) {
        return carJpaRepository.save(car);
    }

    @Override
    public Optional<CarInformation> findById(Long id) {
        return carJpaRepository.findById(id);
    }

    @Override
    public List<CarInformation> findAll() {
        return carJpaRepository.findAll();
    }

    @Override
    public List<CarInformation> findByCarStatus(String status) {
        return carJpaRepository.findByCarStatus(status);
    }

    @Override
    public void delete(CarInformation car) {
        carJpaRepository.delete(car);
    }


}