package com.fpt.rentingservice.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.rentingservice.application.dto.RentCarRequest;
import com.fpt.rentingservice.application.dto.RentingHistoryResponse;
import com.fpt.rentingservice.domain.model.OutboxEvent;
import com.fpt.rentingservice.domain.model.RentingTransaction;
import com.fpt.rentingservice.domain.repository.IOutboxRepository;
import com.fpt.rentingservice.domain.repository.IRentingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentingAppService {

    private final IRentingRepository rentingRepository;
    private final IOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;


    @Transactional
    public void createRenting(RentCarRequest request) {

        RentingTransaction transaction = new RentingTransaction(request.getCustomerId(), Instant.now());

        for (RentCarRequest.CarItem item : request.getCars()) {
            transaction.addCarDetail(item.getCarId(), item.getStartDate(), item.getEndDate(), item.getDailyPrice());
        }

        RentingTransaction savedTx = rentingRepository.save(transaction);
        log.info("Lưu Hóa đơn thành công (PENDING), ID: {}", savedTx.getId());

        try {
            Map<String, Object> payloadData = new HashMap<>();
            payloadData.put("transactionId", savedTx.getId());
            payloadData.put("customerId", savedTx.getCustomerId());
            payloadData.put("cars", request.getCars());
            payloadData.put("eventType", "RENTING_CREATED");

            String jsonPayload = objectMapper.writeValueAsString(payloadData);

            OutboxEvent event = new OutboxEvent(
                    "RentingTransaction",
                    savedTx.getId().toString(),
                    "RENTING_CREATED",
                    jsonPayload
            );
            
            outboxRepository.save(event);
            log.info("Đã thêm thông báo vào Outbox!");

        } catch (Exception e) {
            log.error("Lỗi khi parse JSON Outbox", e);
            throw new RuntimeException("Không thể tạo Outbox Event", e);
        }
    }

    public List<RentingHistoryResponse> getMyHistory(Long customerId) {
        List<RentingTransaction> transactions = rentingRepository.findByCustomerId(customerId);

        return transactions.stream().map(tx -> RentingHistoryResponse.builder()
                .transactionId(tx.getId())
                .rentingDate(tx.getRentingDate())
                .totalPrice(tx.getTotalPrice())
                .status(tx.getRentingStatus())
                .cars(tx.getDetails().stream().map(d -> RentingHistoryResponse.CarDetail.builder()
                        .carId(d.getId().getCarId())
                        .startDate(d.getStartDate())
                        .endDate(d.getEndDate())
                        .price(d.getPrice())
                        .build()).toList())
                .build()
        ).toList();
    }

    public List<RentingHistoryResponse> getStatisticReport(Instant startDate, Instant endDate) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu không thể nằm sau ngày kết thúc!");
        }

        List<RentingTransaction> transactions = rentingRepository.findByRentingDateBetween(startDate, endDate);

        return transactions.stream().map(tx -> RentingHistoryResponse.builder()
                .transactionId(tx.getId())
                .rentingDate(tx.getRentingDate())
                .totalPrice(tx.getTotalPrice())
                .status(tx.getRentingStatus())
                .cars(tx.getDetails().stream().map(d -> RentingHistoryResponse.CarDetail.builder()
                        .carId(d.getId().getCarId())
                        .startDate(d.getStartDate())
                        .endDate(d.getEndDate())
                        .price(d.getPrice())
                        .build()).toList())
                .build()
        ).toList();
    }
}