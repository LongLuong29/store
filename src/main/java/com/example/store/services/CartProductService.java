package com.example.store.services;

import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface CartProductService {
  ResponseEntity<ResponseObject> addProductToCart(Long cartId, Long productId, int amount);
  ResponseEntity<ResponseObject> deleteProductToCart(Long cartId, Long productId);
  ResponseEntity<?> getProductToCart(Long cartId);

  ResponseEntity<ResponseObject> clearProductInCart(Long cartId);
}
