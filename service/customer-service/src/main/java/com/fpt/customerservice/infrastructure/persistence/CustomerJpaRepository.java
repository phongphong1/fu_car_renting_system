package com.fpt.customerservice.infrastructure.persistence;

import com.fpt.customerservice.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CustomerJpaRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByEmail(String email);
    
}