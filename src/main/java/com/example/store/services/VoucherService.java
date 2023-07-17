package com.example.store.services;

import com.example.store.dto.request.VoucherRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.VoucherResponseDTO;
import org.springframework.http.ResponseEntity;

public interface VoucherService {
    ResponseEntity<?> getAllVoucher();

    ResponseEntity<?> decreaseVoucherQuantity(Long voucherId);
    ResponseEntity<ResponseObject> createVoucher(VoucherRequestDTO voucherRequestDTO);
    ResponseEntity<ResponseObject> updateVoucher(VoucherRequestDTO voucherRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteVoucher(Long id);

    VoucherResponseDTO getVoucherById(Long id);
}
