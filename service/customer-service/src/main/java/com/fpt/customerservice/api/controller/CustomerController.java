package com.fpt.customerservice.api.controller;

import com.fpt.customerservice.application.dto.CustomerProfileResponse;
import com.fpt.customerservice.application.dto.LoginRequest;
import com.fpt.customerservice.application.dto.RegisterRequest;
import com.fpt.customerservice.application.dto.UpdateProfileRequest;
import com.fpt.customerservice.application.service.CustomerAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Customer API", description = "Các API liên quan đến thao tác về Khách hàng")
public class CustomerController {

    private final CustomerAppService customerAppService;

    @Operation(summary = "Đăng ký tài khoản mới", description = "API dành cho khách chưa có tài khoản")
    @PostMapping("/register")
    public ResponseEntity<CustomerProfileResponse> register(@Valid @RequestBody RegisterRequest request) {
        CustomerProfileResponse response = customerAppService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Đăng nhập hệ thống", description = "Trả về JWT Token để gọi các API khác")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String token = customerAppService.login(request);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @GetMapping
    public ResponseEntity<?> getAllCustomers(
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {

        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xem danh sách khách hàng!");
        }

        return ResponseEntity.ok(customerAppService.getAllCustomers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId,
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {
        if (!"ROLE_ADMIN".equals(role) && !id.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền xem thông tin chi tiết khách hàng!");
        }
        return ResponseEntity.ok(customerAppService.getCustomerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @Valid @RequestBody UpdateProfileRequest request) {

        if (!"ROLE_ADMIN".equals(role) && !id.equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn chỉ được phép sửa tài khoản của chính mình!");
        }

        return ResponseEntity.ok(customerAppService.updateProfile(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestParam String status) {

        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền sửa trạng thái khách hàng!");
        }

        customerAppService.updateCustomerStatus(id, status);
        return ResponseEntity.ok("Cập nhật trạng thái thành công!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-Role", required = false) String role
    ) {
        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Bạn không có quyền xoá khách hàng!");
        }

        customerAppService.deleteCustomer(id);
        return ResponseEntity.ok("Xóa khách hàng thành công!");
    }
}