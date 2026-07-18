package com.fpt.rentingservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RentingDetailId implements Serializable {
    @Serial
    private static final long serialVersionUID = 4299651101843598109L;
    @jakarta.validation.constraints.NotNull
    @Column(name = "renting_transaction_id", nullable = false)
    private Long rentingTransactionId;

    @jakarta.validation.constraints.NotNull
    @Column(name = "car_id", nullable = false)
    private Long carId;


}