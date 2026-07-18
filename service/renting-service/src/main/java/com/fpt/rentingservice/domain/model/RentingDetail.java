package com.fpt.rentingservice.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
@Table(name = "renting_detail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RentingDetail {
    @EmbeddedId
    private RentingDetailId id;

    @MapsId("rentingTransactionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "renting_transaction_id", nullable = false)
    private RentingTransaction rentingTransaction;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "price", precision = 12, scale = 2)
    private BigDecimal price;

    public RentingDetail(RentingTransaction transaction, Long carId, Instant startDate, Instant endDate, BigDecimal price) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc!");
        }
        this.id = new RentingDetailId(transaction.getId(), carId);
        this.rentingTransaction = transaction;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }
}