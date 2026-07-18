package com.fpt.carservice.application.service;

import com.fpt.carservice.application.dto.CarResponse;
import com.fpt.carservice.application.dto.CreateCarRequest;
import com.fpt.carservice.domain.model.CarInformation;
import com.fpt.carservice.domain.model.Manufacturer;
import com.fpt.carservice.domain.model.Supplier;
import com.fpt.carservice.domain.repository.ICarRepository;
import com.fpt.carservice.domain.repository.IManufacturerRepository;
import com.fpt.carservice.domain.repository.ISupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarAppService {

    private final ICarRepository carRepository;
    private final IManufacturerRepository manufacturerRepository;
    private final ISupplierRepository supplierRepository;

    public CarResponse createCar(CreateCarRequest request) {

        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Hãng sản xuất với ID: " + request.getManufacturerID()));

        Supplier supplier = supplierRepository.findById(request.getSupplierID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Nhà cung cấp với ID: " + request.getSupplierID()));

        CarInformation newCar = new CarInformation(
                request.getCarName(),
                request.getCarDescription(),
                request.getNumberOfDoors(),
                request.getSeatingCapacity(),
                request.getFuelType(),
                request.getYear(),
                manufacturer,
                supplier,
                request.getCarRentingPricePerDay()
        );

        CarInformation savedCar = carRepository.save(newCar);
        log.info("Nhập xe thành công! ID: {}", savedCar.getId());

        return CarResponse.from(savedCar);
    }

    public List<CarResponse> getAllCars() {
        return carRepository.findAll().stream()
                .map(CarResponse::from)
                .toList();
    }

    public List<CarResponse> getAvailableCars() {
        return carRepository.findByCarStatus("AVAILABLE").stream()
                .map(CarResponse::from)
                .toList();
    }

    public CarResponse getCarById(Long id) {
        CarInformation car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe!"));

        return CarResponse.from(car);
    }

    @Transactional
    public CarResponse updateCar(Long id, CreateCarRequest request) {
        CarInformation car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe để sửa!"));

        Manufacturer manufacturer = manufacturerRepository.findById(request.getManufacturerID())
                .orElseThrow(() -> new RuntimeException("Hãng sản xuất không tồn tại!"));

        Supplier supplier = supplierRepository.findById(request.getSupplierID())
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại!"));

        car.updateBasicInformation(
                request.getCarName(),
                request.getCarDescription(),
                request.getNumberOfDoors(),
                request.getSeatingCapacity(),
                request.getFuelType(),
                request.getYear(),
                manufacturer,
                supplier
        );

        if (request.getCarRentingPricePerDay() != null) {
            car.updateRentalPrice(request.getCarRentingPricePerDay());
        }

        carRepository.save(car);

        return CarResponse.from(car);
    }

    @Transactional
    public void deleteCar(Long id) {
        CarInformation car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Xe không tồn tại!"));

        if ("RENTED".equals(car.getCarStatus())) {
            throw new IllegalStateException("Xe đang có khách thuê, không thể xoá!");
        }

        carRepository.delete(car);
    }
}