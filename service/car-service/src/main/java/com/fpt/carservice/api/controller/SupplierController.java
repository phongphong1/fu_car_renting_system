package com.fpt.carservice.api.controller;

import com.fpt.carservice.application.dto.SupplierResponse;
import com.fpt.carservice.application.service.SupplierAppService;
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
@RequestMapping("/api/cars/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier API", description = "Các API liên quan đến quản lý Nhà cung cấp")
public class SupplierController {

    private final SupplierAppService supplierAppService;

    @Operation(summary = "Lấy danh sách nhà cung cấp", description = "API cho phép xem toàn bộ danh sách nhà cung cấp")
    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAllSuppliers() {
        return ResponseEntity.ok(supplierAppService.getAllSuppliers());
    }

    @Operation(summary = "Lấy thông tin nhà cung cấp theo ID", description = "API cho phép xem chi tiết 1 nhà cung cấp")
    @GetMapping("/{id}")
    public ResponseEntity<?> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierAppService.getSupplierById(id));
    }
}