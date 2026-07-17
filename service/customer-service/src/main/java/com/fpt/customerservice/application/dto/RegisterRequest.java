package com.fpt.customerservice.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequest {

    @NotBlank(message = "Tên khách hàng không được để trống!")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống!")
    private String telephone;

    @NotBlank(message = "Email không được để trống!")
    @Email(message = "Email không hợp lệ!")
    private String email;

    @Past(message = "Ngày sinh phải là một ngày trong quá khứ!")
    private LocalDate customerBirthday;

    @NotBlank(message = "Mật khẩu không được để trống!")
    private String password;

}