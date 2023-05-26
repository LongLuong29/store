package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface ProductDiscountService {
  ResponseEntity<ResponseObject> createProductDiscount(Long productId, Long discountId);
  ResponseEntity<?> getProductDiscount(Long productId);
  ResponseEntity<ResponseObject> deleteProductDiscount(Long productId, Long discountId);
}
