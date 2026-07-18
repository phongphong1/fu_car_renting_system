package com.fpt.carservice.application.service;

import com.fpt.carservice.domain.model.OutboxEvent;
import com.fpt.carservice.infrastructure.persistence.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelayService {

    private final OutboxEventRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String TOPIC_NAME = "car-events";

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void pumpEventsToKafka() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByStatusOrderByCreatedAtAsc("PENDING");

        if (!pendingEvents.isEmpty()) {
            log.info("Phát hiện {} sự kiện đang chờ gửi...", pendingEvents.size());
        }

        for (OutboxEvent event : pendingEvents) {
            try {
                kafkaTemplate.send(TOPIC_NAME, event.getAggregateId(), event.getPayload());

                event.markAsPublished();
                outboxRepository.save(event);
                
                log.info("Đã bắn Event ID [{}] lên Kafka Topic [{}]", event.getId(), TOPIC_NAME);
            } catch (Exception e) {
                log.error("Lỗi khi bắn Kafka Event ID: {}", event.getId(), e);
            }
        }
    }
}