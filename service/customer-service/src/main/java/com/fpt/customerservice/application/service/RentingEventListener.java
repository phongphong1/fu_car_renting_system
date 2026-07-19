package com.fpt.customerservice.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.customerservice.domain.model.Customer;
import com.fpt.customerservice.domain.model.OutboxEvent;
import com.fpt.customerservice.domain.repository.ICustomerRepository;
import com.fpt.customerservice.infrastructure.persistence.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentingEventListener {

    private final ICustomerRepository customerRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "renting-events", groupId = "customer-service-group")
    @Transactional
    public void listenToRentingEvents(String payload) {
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            Long transactionId = jsonNode.get("transactionId").asLong();
            Long customerId = jsonNode.get("customerId").asLong();


            Customer customer = customerRepository.findById(customerId).orElse(null);

            // kiểm tra trạng thái khách hàng
            if (customer == null || "BANNED".equals(customer.getCustomerStatus())) {

                ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put("eventType", "CUSTOMER_BANNED");
                String replyPayload = objectMapper.writeValueAsString(jsonNode);
                OutboxEvent replyEvent = new OutboxEvent(
                        "RentingTransaction",
                        transactionId.toString(),
                        "CUSTOMER_BANNED",
                        replyPayload
                );
                outboxRepository.save(replyEvent);
            }
        } catch (Exception e) {
            log.error("Lỗi khi đọc Kafka event bên Customer", e);
        }
    }
}