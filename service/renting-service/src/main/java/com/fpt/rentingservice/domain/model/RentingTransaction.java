package com.fpt.rentingservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "renting_transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentingTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "renting_transaction_id", nullable = false)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "renting_date")
    private Instant rentingDate;

    @Column(name = "total_price", precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @NotNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Size(max = 50)
    @Column(name = "renting_status", length = 50)
    private String rentingStatus;

    @OneToMany(mappedBy = "rentingTransaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RentingDetail> details = new ArrayList<>();

    public RentingTransaction(Long customerId, Instant rentingDate) {
        this.customerId = customerId;
        this.rentingDate = rentingDate;
        this.totalPrice = BigDecimal.ZERO;
        this.rentingStatus = "PENDING";
    }

    public void addCarDetail(Long carId, Instant startDate, Instant endDate, BigDecimal dailyPrice) {
        boolean carExists = details.stream().anyMatch(d -> d.getId().getCarId().equals(carId));
        if (carExists) {
            throw new IllegalArgumentException("Xe hiện tại không khả dụng!");
        }
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        long rentalDays = daysBetween == 0 ? 1 : daysBetween;

        BigDecimal carTotalPrice = dailyPrice.multiply(BigDecimal.valueOf(rentalDays));
        this.totalPrice = this.totalPrice.add(carTotalPrice);

        RentingDetail detail = new RentingDetail(this, carId, startDate, endDate, dailyPrice);
        this.details.add(detail);
    }

    public void cancelTransaction() {
        if ("COMPLETED".equals(this.rentingStatus)) {
            throw new IllegalStateException("Giao dịch đã hoàn thành, không thể huỷ!");
        }
        this.rentingStatus = "CANCELLED";
    }

    public void completeTransaction() {
        if (!"PENDING".equals(this.rentingStatus)) {
            throw new IllegalStateException("Giao dịch không ở trạng thái chờ, không thể hoàn thành!");
        }
        this.rentingStatus = "COMPLETED";
    }
}