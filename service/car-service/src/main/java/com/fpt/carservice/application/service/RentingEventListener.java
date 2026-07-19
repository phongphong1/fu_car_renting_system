package com.fpt.carservice.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.carservice.domain.model.CarInformation;
import com.fpt.carservice.domain.model.OutboxEvent;
import com.fpt.carservice.domain.repository.ICarRepository;
import com.fpt.carservice.infrastructure.persistence.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RentingEventListener {

    private final ICarRepository carRepository;
    private final OutboxEventRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "renting-events", groupId = "car-service-group")
    @Transactional
    public void listenToRentingEvents(String payload) {
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            Long transactionId = jsonNode.get("transactionId").asLong();
            JsonNode carsArray = jsonNode.get("cars");

            boolean isAllCarsAvailable = true;
            List<CarInformation> carsToRent = new ArrayList<>();

            for (JsonNode carNode : carsArray) {
                Long carId = carNode.get("carId").asLong();
                CarInformation car = carRepository.findById(carId).orElse(null);

                BigDecimal dailyPrice = carNode.get("dailyPrice").decimalValue();
                // Nếu xe không tồn tại, không ở trạng thái Sẵn sàng hoặc đã bị thay đổi giá
                if (car == null
                        || !"AVAILABLE".equals(car.getCarStatus())
                        || car.getCarRentingPricePerDay().compareTo(dailyPrice) != 0
                ) {
                    isAllCarsAvailable = false;
                    break;
                }
                carsToRent.add(car);
            }

            String eventType;

            if (isAllCarsAvailable) {
                for (CarInformation car : carsToRent) {
                    car.changeStatus("RENTED");
                    carRepository.save(car);
                }
                eventType = "CAR_RESERVED_SUCCESS";
            } else {
                eventType = "CAR_RESERVED_FAILED";
            }

            ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put("eventType", eventType);
            String replyPayload = objectMapper.writeValueAsString(jsonNode);
            OutboxEvent replyEvent = new OutboxEvent(
                    "RentingTransaction",
                    transactionId.toString(),
                    eventType,
                    replyPayload
            );
            outboxRepository.save(replyEvent);

        } catch (Exception e) {
            log.error("Lỗi khi xử lý Kafka event bên Car Service", e);
        }
    }

    @KafkaListener(topics = "renting-events", groupId = "car-service-rollback-group")
    @Transactional
    public void listenToCancelEvents(String payload) {
        try {
            if (payload != null && payload.contains("RENTING_CANCELLED")) {
                JsonNode jsonNode = objectMapper.readTree(payload);

                if (jsonNode.has("cars") && jsonNode.get("cars").isArray()) {
                    JsonNode carsArray = jsonNode.get("cars");

                    for (JsonNode carNode : carsArray) {
                        Long carId = carNode.get("carId").asLong();
                        carRepository.findById(carId).ifPresent(car -> {
                            if ("RENTED".equals(car.getCarStatus())) {
                                car.changeStatus("AVAILABLE");
                                carRepository.save(car);
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            log.error("Lỗi thả xe bên Car Service", e);
        }
    }
}