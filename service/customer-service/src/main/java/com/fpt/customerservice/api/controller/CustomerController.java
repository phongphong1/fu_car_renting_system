package com.fpt.customerservice.api.controller;

import com.fpt.customerservice.application.dto.CustomerProfileResponse;
import com.fpt.customerservice.application.dto.LoginRequest;
import com.fpt.customerservice.application.dto.RegisterRequest;
import com.fpt.customerservice.application.dto.UpdateProfileRequest;
import com.fpt.customerservice.application.service.CustomerAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerAppService customerAppService;

    @PostMapping("/register")
    public ResponseEntity<CustomerProfileResponse> register(@Valid @RequestBody RegisterRequest request) {
        CustomerProfileResponse response = customerAppService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = customerAppService.login(request);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping
    public ResponseEntity<List<CustomerProfileResponse>> getAllCustomers() {
        return ResponseEntity.ok(customerAppService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerProfileResponse> getCustomerById(@PathVariable Long id) {
        return ResponseEntity.ok(customerAppService.getCustomerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerProfileResponse> updateProfile(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(customerAppService.updateProfile(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        customerAppService.updateCustomerStatus(id, status);
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Long id) {
        customerAppService.deleteCustomer(id);
        return ResponseEntity.ok("Xóa khách hàng thành công!");
    }
}