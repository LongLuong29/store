package com.example.store.controller;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/order")
public class OrderController {
  @Autowired private OrderService orderService;

  @GetMapping(value = "/user")
  public ResponseEntity<?> getOrderByUser(@RequestParam(name = "userId") Long userId){
    return orderService.getOrderByUser(userId);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getOrderById(@PathVariable(name = "id") Long id){

    return orderService.getOrderById(id);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createOrder(@RequestParam(name = "userId") Long userId,
      @RequestBody OrderRequestDTO orderRequestDTO){
    return orderService.createOrder(userId, orderRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateOrder(@RequestParam(name = "orderId") Long orderId,
      @RequestBody OrderRequestDTO orderRequestDTO){
    return orderService.upateOrder(orderId, orderRequestDTO);
  }

  @GetMapping(value = "")
  public ResponseEntity<?> getAllOrder(){
    return orderService.getAllOrder();
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteOrder(@RequestParam(name = "orderId") Long orderId){
    return orderService.deleteOrder(orderId);
  }
}
