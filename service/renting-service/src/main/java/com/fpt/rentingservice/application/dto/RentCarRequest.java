package com.fpt.rentingservice.application.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class RentCarRequest {

    private Long customerId;
    private List<CarItem> cars;

    @Data
    public static class CarItem {
        private Long carId;
        private Instant startDate;
        private Instant endDate;
        private BigDecimal dailyPrice;
    }
}