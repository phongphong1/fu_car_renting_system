package com.fpt.rentingservice.application.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class RentingHistoryResponse {
    private Long transactionId;
    private Instant rentingDate;
    private BigDecimal totalPrice;
    private String status;
    private List<CarDetail> cars;

    @Data
    @Builder
    public static class CarDetail {
        private Long carId;
        private Instant startDate;
        private Instant endDate;
        private BigDecimal price;
    }
}