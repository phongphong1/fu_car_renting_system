package com.fpt.carservice.api.controller;

import com.fpt.carservice.application.dto.ManufacturerResponse;
import com.fpt.carservice.application.service.ManufacturerAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cars/manufacturers")
@RequiredArgsConstructor
@Tag(name = "Manufacturer API", description = "Các API liên quan đến quản lý Nhà sản xuất")
public class ManufacturerController {

    private final ManufacturerAppService manufacturerAppService;

    @Operation(summary = "Lấy danh sách nhà sản xuất", description = "API cho phép xem toàn bộ danh sách nhà sản xuất")
    @GetMapping
    public ResponseEntity<List<ManufacturerResponse>> getAllManufacturers() {
        return ResponseEntity.ok(manufacturerAppService.getAllManufacturers());
    }

    @Operation(summary = "Lấy thông tin nhà sản xuất theo ID", description = "API cho phép xem chi tiết 1 nhà sản xuất")
    @GetMapping("/{id}")
    public ResponseEntity<?> getManufacturerById(@PathVariable Long id) {
        return ResponseEntity.ok(manufacturerAppService.getManufacturerById(id));
    }
}