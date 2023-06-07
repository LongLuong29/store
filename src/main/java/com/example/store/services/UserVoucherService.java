package com.example.store.services;

import com.example.store.dto.request.VoucherRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.dto.response.UserVoucherResponseDTO;
import com.example.store.dto.response.VoucherResponseDTO;
import org.springframework.http.ResponseEntity;

public interface UserVoucherService {
    ResponseEntity<?> getAllUserVoucher(Long userId);
    ResponseEntity<ResponseObject> createUserVoucher(Long userId, Long voucherId);
    ResponseEntity<ResponseObject> deleteUserVoucher(Long userId, Long voucherId);

}
