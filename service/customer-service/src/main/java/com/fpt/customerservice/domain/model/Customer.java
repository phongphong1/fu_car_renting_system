package com.fpt.customerservice.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "customer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id", nullable = false)
    private Long id;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "customer_birthday")
    private LocalDate customerBirthday;

    @Column(name = "customer_status", length = 50)
    private String customerStatus;

    @Column(name = "password", nullable = false)
    private String password;

    public Customer(String customerName, String telephone, String email, LocalDate customerBirthday, String password) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email không được để trống!");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password không được để trống!");
        }

        this.customerName = customerName;
        this.telephone = telephone;
        this.email = email;
        this.customerBirthday = customerBirthday;
        this.password = password;
        this.customerStatus = "ACTIVE";
    }

    public void updateProfile(String newName, String newTelephone, LocalDate newBirthday) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên khách hàng không được để trống!");
        }
        this.customerName = newName;
        this.telephone = newTelephone;
        this.customerBirthday = newBirthday;
    }

    public void updateStatus(String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Trạng thái không hợp lệ!");
        }
        this.customerStatus = newStatus;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.customerStatus);
    }
}