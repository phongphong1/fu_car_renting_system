package com.fpt.carservice.api.controller;

import com.fpt.carservice.application.dto.CarResponse;
import com.fpt.carservice.application.dto.CreateCarRequest;
import com.fpt.carservice.application.service.CarAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Car API", description = "Các API liên quan đến quản lý Xe")
public class CarController {

    private final CarAppService carAppService;

    @Operation(summary = "Tạo xe mới", description = "API dành cho ADMIN để thêm xe vào hệ thống")
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

    @Operation(summary = "Lấy tất cả xe", description = "API dành cho ADMIN để xem toàn bộ danh sách xe")
    @GetMapping
    public ResponseEntity<?> getAllCars(
            @RequestHeader(value = "X-User-Role", required = false) String role) {
        
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền thực hiện hành động này!");
        }

        return ResponseEntity.ok(carAppService.getAllCars());
    }

    @Operation(summary = "Lấy danh sách xe khả dụng", description = "API cho phép mọi người dùng xem danh sách xe đang rảnh")
    @GetMapping("/available")
    public ResponseEntity<List<CarResponse>> getAvailableCars() {
        return ResponseEntity.ok(carAppService.getAvailableCars());
    }

    @Operation(summary = "Lấy thông tin xe theo ID", description = "API cho phép xem chi tiết 1 xe")
    @GetMapping("/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carAppService.getCarById(id));
    }

    @Operation(summary = "Cập nhật thông tin xe", description = "API dành cho ADMIN để sửa thông tin xe")
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

    @Operation(summary = "Xoá xe", description = "API dành cho ADMIN để xoá xe khỏi hệ thống")
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