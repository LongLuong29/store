package com.example.store.services;

import com.example.store.dto.request.AttributeRequestDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface ProductAttributeService {
  ResponseEntity<ResponseObject> createProductAttribute(Long productId, AttributeRequestDTO attributeRequestDTO);
  ResponseEntity<ResponseObject> updateProductAttribute(Long productId, Long attributeId, AttributeRequestDTO attributeRequestDTO);
  ResponseEntity<?> getProductAttributeByProduct(Long productId);
  ResponseEntity<?> softDelete(Long productId, boolean deleted);

}
