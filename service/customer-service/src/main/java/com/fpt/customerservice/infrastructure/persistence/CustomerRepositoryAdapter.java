package com.fpt.customerservice.infrastructure.persistence;

import com.fpt.customerservice.domain.model.Customer;
import com.fpt.customerservice.domain.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements ICustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        return customerJpaRepository.save(customer);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerJpaRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        customerJpaRepository.deleteById(id);
    }
}