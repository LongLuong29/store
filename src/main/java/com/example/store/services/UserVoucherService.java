package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface UserVoucherService {
    ResponseEntity<?> getAllUserVoucher(Long userId);
    ResponseEntity<?> getUserVouchersForOrder(Long userId, Long orderId);
    ResponseEntity<ResponseObject> getUserVoucherByCode(Long userId, Long orderId, String code);
    ResponseEntity<ResponseObject> createUserVoucher(Long userId, Long voucherId);
    ResponseEntity<ResponseObject> deleteUserVoucher(Long userId, Long voucherId);
    BigDecimal calculateVoucherDiscount(Long userId, Long voucherId, BigDecimal totalPrice, BigDecimal shippingFee);
}
