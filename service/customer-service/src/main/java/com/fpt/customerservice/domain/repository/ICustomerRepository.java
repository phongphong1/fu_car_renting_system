package com.fpt.customerservice.domain.repository;

import com.fpt.customerservice.domain.model.Customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    Optional<Customer> findByEmail(String email);

    List<Customer> findAll();

    void deleteById(Long id);
}
