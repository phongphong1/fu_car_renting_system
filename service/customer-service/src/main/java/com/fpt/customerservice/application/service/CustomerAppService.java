package com.fpt.customerservice.application.service;


import com.fpt.customerservice.application.dto.CustomerProfileResponse;
import com.fpt.customerservice.application.dto.LoginRequest;
import com.fpt.customerservice.application.dto.RegisterRequest;
import com.fpt.customerservice.application.dto.UpdateProfileRequest;
import com.fpt.customerservice.domain.exception.CustomerNotFoundException;
import com.fpt.customerservice.domain.model.Customer;
import com.fpt.customerservice.domain.repository.ICustomerRepository;
import com.fpt.customerservice.infrastructure.config.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerAppService {

    private final ICustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    public String login(LoginRequest request) {

        if (request.getEmail().equals(adminEmail) && request.getPassword().equals(adminPassword)) {
            return jwtUtils.generateToken(adminEmail, "ROLE_ADMIN", null);
        }

        Customer customer = customerRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomerNotFoundException("Tài khoản không tồn tại hoặc sai email!"));

        if (!customer.isActive()) {
            throw new RuntimeException("Tài khoản của bạn đã bị khóa!");
        }

        if (!passwordEncoder.matches(request.getPassword(), customer.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }

        return jwtUtils.generateToken(customer.getEmail(), "ROLE_CUSTOMER", customer.getId());
    }


    public CustomerProfileResponse register(RegisterRequest request) {
        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email này đã được sử dụng!");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Customer newCustomer = new Customer(
                request.getCustomerName(),
                request.getTelephone(),
                request.getEmail(),
                request.getCustomerBirthday(),
                hashedPassword
        );

        Customer savedCustomer = customerRepository.save(newCustomer);

        return CustomerProfileResponse.from(savedCustomer);
    }


    public CustomerProfileResponse updateProfile(Long customerId, UpdateProfileRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Không tìm thấy khách hàng với ID: " + customerId));

        customer.updateProfile(request.getCustomerName(), request.getTelephone(), request.getCustomerBirthday());

        Customer updatedCustomer = customerRepository.save(customer);
        return CustomerProfileResponse.from(updatedCustomer);
    }


    public java.util.List<CustomerProfileResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerProfileResponse::from)
                .toList();
    }


    public CustomerProfileResponse getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Không tìm thấy khách hàng!"));
        return CustomerProfileResponse.from(customer);
    }


    public void updateCustomerStatus(Long customerId, String newStatus) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Không tìm thấy khách hàng!"));

        customer.updateStatus(newStatus);
        customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {
        if (customerRepository.findById(customerId).isEmpty()) {
            throw new CustomerNotFoundException("Không tìm thấy khách hàng để xóa!");
        }
        customerRepository.deleteById(customerId);
    }
}