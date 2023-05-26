package com.example.store.services;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface OrderService {
  ResponseEntity<?> getOrderByUser(Long userId);
  ResponseEntity<ResponseObject> createOrder(Long userId, OrderRequestDTO oderRequestDTO);
  ResponseEntity<ResponseObject> upateOrder(Long orderId, OrderRequestDTO orderRequestDTO);
  ResponseEntity<?> getAllOrder();
  ResponseEntity<ResponseObject> deleteOrder(Long orderId);
  ResponseEntity<?> getOrderById(Long id);
}
