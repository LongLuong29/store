package com.example.store.services;

import com.example.store.dto.response.CartResponseDTO;
import org.springframework.http.ResponseEntity;

public interface CartService {
  ResponseEntity<CartResponseDTO> getCartByUser(Long userId);
}
