package com.example.store.services;

import com.example.store.dto.request.GroupProductRequestDTO;
import com.example.store.dto.response.GroupProductResponseDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface GroupProductService {
//    ResponseEntity<?> getAllBrandOnTrading(Pageable pageable);

    ResponseEntity<?> getAllGroupProduct();

    ResponseEntity<ResponseObject> createGroupProduct(GroupProductRequestDTO groupProductRequestDTO);

    ResponseEntity<ResponseObject> updateGroupProduct(GroupProductRequestDTO groupProductRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteGroupProduct(Long id);

    GroupProductResponseDTO getGroupProductById(Long id);
}

