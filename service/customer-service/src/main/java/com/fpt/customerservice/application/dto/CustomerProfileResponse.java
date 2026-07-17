package com.fpt.customerservice.application.dto;

import com.fpt.customerservice.domain.model.Customer;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class CustomerProfileResponse {

    private Long id;
    private String customerName;
    private String telephone;
    private String email;
    private LocalDate customerBirthday;
    private String customerStatus;

    public static CustomerProfileResponse from(Customer customer) {
        if (customer == null) {
            return null;
        }
        return CustomerProfileResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .telephone(customer.getTelephone())
                .email(customer.getEmail())
                .customerBirthday(customer.getCustomerBirthday())
                .customerStatus(customer.getCustomerStatus())
                .build();
    }
}