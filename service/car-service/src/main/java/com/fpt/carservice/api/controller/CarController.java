package com.fpt.carservice.api.controller;

import com.fpt.carservice.application.dto.CarResponse;
import com.fpt.carservice.application.dto.CreateCarRequest;
import com.fpt.carservice.application.service.CarAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Slf4j
public class CarController {

    private final CarAppService carAppService;

    @PostMapping
    public ResponseEntity<?> createCar(
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody CreateCarRequest request) {

        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thực hiện hành động này!");
        }

        CarResponse response = carAppService.createCar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllCars(
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thực hiện hành động này!");
        }

        return ResponseEntity.ok(carAppService.getAllCars());
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarResponse>> getAvailableCars() {
        return ResponseEntity.ok(carAppService.getAvailableCars());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carAppService.getCarById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestBody CreateCarRequest request) {

        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền chỉnh sửa thông tin xe!");
        }

        return ResponseEntity.ok(carAppService.updateCar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền xoá xe!");
        }

        carAppService.deleteCar(id);
        return ResponseEntity.ok("Đã xoá xe thành công!");
    }
}