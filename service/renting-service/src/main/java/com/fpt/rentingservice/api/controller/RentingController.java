package com.fpt.rentingservice.api.controller;

import com.fpt.rentingservice.application.dto.RentCarRequest;
import com.fpt.rentingservice.application.service.RentingAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/renting")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Renting API", description = "Các API liên quan đến thao tác thuê xe và lịch sử thuê xe")
public class RentingController {

    private final RentingAppService rentingAppService;

    @Operation(summary = "Tạo yêu cầu thuê xe", description = "API dành cho khách hàng để tạo mới 1 yêu cầu thuê xe")
    @PostMapping
    public ResponseEntity<Map<String, String>> createRentingTransaction(
            @RequestHeader(value = "X-User-Id", required = false) Long userIdFromGateway,
            @RequestBody RentCarRequest request) {

        if (userIdFromGateway != null) {
            request.setCustomerId(userIdFromGateway);
        } else {
            log.warn("Không tìm thấy X-User-Id trong Header, đang dùng ID từ payload...");
        }
        rentingAppService.createRenting(request);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Yêu cầu thuê xe đã được tiếp nhận! Hệ thống đang kiểm tra xe, vui lòng chờ trong giây lát.");
        response.put("status", "PENDING");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Operation(summary = "Lấy lịch sử thuê xe", description = "API dành cho khách hàng xem lại các xe mình đã thuê")
    @GetMapping("/history")
    public ResponseEntity<?> getMyRentingHistory(
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {

        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không xác định được danh tính người dùng!");
        }

        return ResponseEntity.ok(rentingAppService.getMyHistory(currentUserId));
    }

    @Operation(summary = "Báo cáo thống kê", description = "API dành cho ADMIN để xem báo cáo doanh thu, số lượng xe thuê theo thời gian")
    @GetMapping("/report")
    public ResponseEntity<?> getStatisticReport(
            @RequestParam("startDate") Instant startDate,
            @RequestParam("endDate") Instant endDate,
            @RequestHeader(value = "X-User-Role", required = false) String role) {

        if (!"ROLE_ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Bạn không có quyền xem báo cáo thống kê.");
        }

        return ResponseEntity.ok(rentingAppService.getStatisticReport(startDate, endDate));
    }
}