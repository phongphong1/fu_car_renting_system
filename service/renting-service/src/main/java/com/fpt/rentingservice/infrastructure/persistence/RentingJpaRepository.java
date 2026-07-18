package com.fpt.rentingservice.infrastructure.persistence;

import com.fpt.rentingservice.domain.model.RentingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface RentingJpaRepository extends JpaRepository<RentingTransaction, Long> {

    List<RentingTransaction> findByCustomerIdOrderByRentingDateDesc(Long customerId);

    List<RentingTransaction> findByRentingDateBetweenOrderByRentingDateDesc(Instant startDate, Instant endDate);
}