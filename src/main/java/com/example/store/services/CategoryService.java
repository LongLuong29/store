package com.example.store.services;

import com.example.store.dto.request.CategoryRequestDTO;
import com.example.store.dto.response.CategoryResponseDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface CategoryService {
    ResponseEntity<?> getAllCategoryOnTrading(Pageable pageable);
    ResponseEntity<?> getAllCategory();
    ResponseEntity<?> getCategoryByGroupProduct(Long groupProductId);
    ResponseEntity<ResponseObject> createCategory(CategoryRequestDTO categoryRequestDTO);
    ResponseEntity<ResponseObject> updateCategory(CategoryRequestDTO categoryRequestDTO, Long id);
    ResponseEntity<ResponseObject> deleteCategory(Long id);
    ResponseEntity<ResponseObject> safeDelete(Long id, boolean deleted);
    CategoryResponseDTO getCategoryById(Long id);
}
