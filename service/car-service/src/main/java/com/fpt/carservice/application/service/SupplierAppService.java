package com.fpt.carservice.application.service;

import com.fpt.carservice.application.dto.SupplierResponse;
import com.fpt.carservice.domain.model.Supplier;
import com.fpt.carservice.domain.repository.ISupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierAppService {

    private final ISupplierRepository supplierRepository;

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(SupplierResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierResponse getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhà cung cấp với ID: " + id)); // Thay bằng Exception custom của bạn nếu có
        return SupplierResponse.fromDomain(supplier);
    }
}