package com.fpt.rentingservice.infrastructure.persistence;

import com.fpt.rentingservice.domain.model.RentingTransaction;
import com.fpt.rentingservice.domain.repository.IRentingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RentingRepositoryAdapter implements IRentingRepository {

    private final RentingJpaRepository jpaRepository;

    @Override
    public RentingTransaction save(RentingTransaction transaction) {
        return jpaRepository.save(transaction);
    }

    @Override
    public Optional<RentingTransaction> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<RentingTransaction> findByCustomerId(Long customerId) {
        return jpaRepository.findByCustomerIdOrderByRentingDateDesc(customerId);
    }

    @Override
    public List<RentingTransaction> findByRentingDateBetween(Instant startDate, Instant endDate) {
        return jpaRepository.findByRentingDateBetweenOrderByRentingDateDesc(startDate, endDate);
    }
}