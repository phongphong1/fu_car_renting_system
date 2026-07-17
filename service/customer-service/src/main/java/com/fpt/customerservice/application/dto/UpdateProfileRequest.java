package com.fpt.customerservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfileRequest {

    @NotBlank(message = "Tên khách hàng không được để trống!")
    private String customerName;

    @NotBlank(message = "Số điện thoại không được để trống!")
    private String telephone;

    @Past(message = "Ngày sinh phải là một ngày trong quá khứ!")
    private LocalDate customerBirthday;
}