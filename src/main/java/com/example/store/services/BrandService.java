package com.example.store.services;

import com.example.store.dto.request.BrandRequestDTO;
import com.example.store.dto.response.BrandResponseDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface BrandService {
//    ResponseEntity<?> getAllBrandOnTrading(Pageable pageable);

    ResponseEntity<?> getAllBrand();

    ResponseEntity<ResponseObject> createBrand(BrandRequestDTO brandRequestDTO);

    ResponseEntity<ResponseObject> updateBrand(BrandRequestDTO brandRequestDTO, Long id);

    ResponseEntity<ResponseObject> deleteBrand(Long id);

    BrandResponseDTO getBrandById(Long id);
}

