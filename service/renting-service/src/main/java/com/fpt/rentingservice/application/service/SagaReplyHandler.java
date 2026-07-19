package com.fpt.rentingservice.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fpt.rentingservice.domain.model.OutboxEvent;
import com.fpt.rentingservice.domain.model.RentingTransaction;
import com.fpt.rentingservice.domain.repository.IOutboxRepository;
import com.fpt.rentingservice.domain.repository.IRentingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SagaReplyHandler {

    private final IRentingRepository rentingRepository;
    private final IOutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = {"customer-events", "car-events"}, groupId = "renting-saga-group")
    @Transactional
    public void handleSagaReplies(String payload) {
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);

            Long transactionId = jsonNode.get("transactionId").asLong();

            RentingTransaction transaction = rentingRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Hóa đơn!"));

            if (!"PENDING".equals(transaction.getRentingStatus())) {
                return;
            }

            if (payload.contains("CUSTOMER_BANNED") || payload.contains("CAR_RESERVED_FAILED")) {

                transaction.cancelTransaction();
                rentingRepository.save(transaction);

                if (payload.contains("CUSTOMER_BANNED")) {

                    ((ObjectNode) jsonNode).put("eventType", "RENTING_CANCELLED");
                    String cancelPayload = objectMapper.writeValueAsString(jsonNode);

                    OutboxEvent cancelEvent = new OutboxEvent(
                            "RentingTransaction",
                            transactionId.toString(),
                            "RENTING_CANCELLED",
                            cancelPayload
                    );
                    outboxRepository.save(cancelEvent);
                }
            }
            else if (payload.contains("CAR_RESERVED_SUCCESS")) {
                transaction.completeTransaction();
                rentingRepository.save(transaction);
            }

        } catch (Exception e) {
            log.error("Lỗi khi xử lý kết quả", e);
        }
    }
}