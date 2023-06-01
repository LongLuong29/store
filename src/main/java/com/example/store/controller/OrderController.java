package com.example.store.controller;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.OrderProductService;
import com.example.store.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/order")
public class OrderController {
  @Autowired private OrderService orderService;
  @Autowired private OrderProductService orderProductService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllOrder(){
    return orderService.getAllOrder();
  }

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


  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteOrder(@RequestParam(name = "orderId") Long orderId){
    return orderService.deleteOrder(orderId);
  }

  // *****************************************************
  // ORDER-PRODUCT API ***********************************
  @GetMapping(value = "/product")
  public ResponseEntity<?> getOrderProduct(@RequestParam(name = "orderId") Long orderId) {
    return orderProductService.getProductByOrder(orderId);
  }

  @GetMapping(value = "/product/paid")
  public ResponseEntity<?> getAllProductToBillPayed() {
    return orderProductService.getAllProductByOrderPayed();
  }

  @PostMapping(value = "/orderProduct")
    public ResponseEntity<ResponseObject> addProductToOrder(@RequestParam(name = "orderId") Long orderId,
                                                         @RequestParam(name = "productId") Long productId,
                                                          @RequestParam(name = "amount") int amount) {
    return orderProductService.addProductToOrder(orderId, productId, amount);
  }
}
