package com.fpt.carservice.application.service;

import com.fpt.carservice.application.dto.ManufacturerResponse;
import com.fpt.carservice.domain.model.Manufacturer;
import com.fpt.carservice.domain.repository.IManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManufacturerAppService {

    private final IManufacturerRepository manufacturerRepository;

    @Transactional(readOnly = true)
    public List<ManufacturerResponse> getAllManufacturers() {
        return manufacturerRepository.findAll().stream()
                .map(ManufacturerResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ManufacturerResponse getManufacturerById(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà sản xuất với ID: " + id)); // Thay bằng Exception custom của bạn nếu có
        return ManufacturerResponse.fromDomain(manufacturer);
    }
}