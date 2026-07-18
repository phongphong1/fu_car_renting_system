package com.fpt.rentingservice.domain.repository;

import com.fpt.rentingservice.domain.model.RentingTransaction;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IRentingRepository {

    RentingTransaction save(RentingTransaction transaction);

    Optional<RentingTransaction> findById(Long id);

    List<RentingTransaction> findByCustomerId(Long customerId);

    List<RentingTransaction> findByRentingDateBetween(Instant startDate, Instant endDate);
    
}