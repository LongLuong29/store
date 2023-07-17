package com.example.store.controller;

import com.example.store.dto.response.ResponseObject;
import com.example.store.services.ProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(value = "/api/v1/discount")
public class DiscountController {
  @Autowired private ProductDiscountService productDiscountService;

  // Product-Discount
  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> createProductDiscount(@RequestParam(name = "productId") Long productId,
                                                              @RequestParam(name = "discountId") Long discountId,
                                                              @Param(value = "startDate") Date startDate,
                                                              @Param(value = "endDate") Date endDate){
    return productDiscountService.createProductDiscount(productId, discountId,startDate,endDate);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getProductDiscount(@RequestParam(name = "productId") Long productId){
    return productDiscountService.getProductDiscount(productId);
  }

  @DeleteMapping(value = "/product/softDelete")
  public ResponseEntity<ResponseObject> softDeleteProductDiscount(@RequestParam(name = "productId") Long productId,
                                                              @RequestParam(name = "discountId") Long discountId,
                                                                  @RequestParam(name = "deleted") boolean deleted){
    return productDiscountService.softDeleteProductDiscount(productId, discountId, deleted);
  }

  @DeleteMapping(value = "/product")
  public ResponseEntity<ResponseObject> deleteProductDiscount(@RequestParam(name = "productId") Long productId,
                                                              @RequestParam(name = "discountId") Long discountId){
    return productDiscountService.deleteProductDiscount(productId, discountId);
  }

}

