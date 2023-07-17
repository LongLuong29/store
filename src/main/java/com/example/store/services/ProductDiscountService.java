package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface ProductDiscountService {
  ResponseEntity<ResponseObject> createProductDiscount(Long productId, Long discountId, Date startDate, Date endDate);
  ResponseEntity<?> getProductDiscount(Long productId);
  ResponseEntity<ResponseObject> softDeleteProductDiscount(Long productId, Long discountId, boolean deleted);
  ResponseEntity<ResponseObject> deleteProductDiscount(Long productId, Long discountId);
}
