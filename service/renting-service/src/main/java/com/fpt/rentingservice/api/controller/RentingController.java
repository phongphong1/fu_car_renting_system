package com.fpt.rentingservice.api.controller;

import com.fpt.rentingservice.application.dto.RentCarRequest;
import com.fpt.rentingservice.application.service.RentingAppService;
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
public class RentingController {

    private final RentingAppService rentingAppService;

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

    @GetMapping("/history")
    public ResponseEntity<?> getMyRentingHistory(
            @RequestHeader(value = "X-User-Id", required = false) Long currentUserId) {

        if (currentUserId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Không xác định được danh tính người dùng!");
        }

        return ResponseEntity.ok(rentingAppService.getMyHistory(currentUserId));
    }

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