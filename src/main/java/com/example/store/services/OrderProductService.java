package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface OrderProductService {
  ResponseEntity<ResponseObject> addProductToOrder(Long orderId, Long productId, int amount);
  ResponseEntity<ResponseObject> deleteProductToOrder(Long orderId, Long productId);
  ResponseEntity<?> getProductByOrder(Long orderId);
  ResponseEntity<?> getAllProductByOrderPayed();
}
