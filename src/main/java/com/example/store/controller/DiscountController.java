package com.example.store.controller;

import com.example.store.dto.response.ResponseObject;
import com.example.store.services.ProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/discount")
public class DiscountController {
  @Autowired private ProductDiscountService productDiscountService;

  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> createProductDiscount(@RequestParam(name = "productId") Long productId,
                                                              @RequestParam(name = "discountId") Long discountId){
    return productDiscountService.createProductDiscount(productId, discountId);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getProductDiscount(@RequestParam(name = "productId") Long productId){
    return productDiscountService.getProductDiscount(productId);
  }

  @DeleteMapping(value = "/product")
  public ResponseEntity<ResponseObject> deleteProductDiscount(@RequestParam(name = "productId") Long productId, @RequestParam(name = "discountId") Long discountId){
    return productDiscountService.deleteProductDiscount(productId, discountId);
  }
}
