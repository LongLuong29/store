package com.example.store.services;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Order;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface OrderService {
  ResponseEntity<?> getOrderByUser(Long userId);

  ResponseEntity<ResponseObject> createOrder(Long userId, OrderRequestDTO oderRequestDTO/*, Long discountId*/);

  ResponseEntity<ResponseObject> upateOrder(Long orderId, OrderRequestDTO orderRequestDTO);

  ResponseEntity<?> getAllOrder();

  ResponseEntity<ResponseObject> deleteOrder(Long orderId);

  ResponseEntity<?> getOrderById(Long id);

  ResponseEntity<ResponseObject> updateOrderStatus(Long orderId, String orderStatus);

  void sendEmailForOrderStatus(Order order, int typeMail) throws MessagingException, UnsupportedEncodingException;
}
