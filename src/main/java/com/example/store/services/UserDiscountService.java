package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface UserDiscountService {
  ResponseEntity<ResponseObject> createUserDiscount(Long userId, Long discountId);
  ResponseEntity<?> getUserDiscount(Long userId);
  ResponseEntity<ResponseObject> deleteUserDiscount(Long userId, Long discountId);
}
