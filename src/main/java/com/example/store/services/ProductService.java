package com.example.store.services;

import com.example.store.dto.request.ProductRequestDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface ProductService {
  ResponseEntity<?> getAllProductOnTrading(Pageable pageable);
  ResponseEntity<ResponseObject> createProduct(ProductRequestDTO productRequestDTO);
  ResponseEntity<ResponseObject> updateProduct(ProductRequestDTO productRequestDTO, Long id);
  ResponseEntity<ResponseObject> deleteProduct(Long id);
  ResponseEntity<?> getProductById(Long id);
  ResponseEntity<?> search(String search, int page, int size);
  ResponseEntity<ResponseObject> softDelete(Long id, boolean deleted);
  ResponseEntity<ResponseObject> setForSale(Long id, boolean forSale);

  }
