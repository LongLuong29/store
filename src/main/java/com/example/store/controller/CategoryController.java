package com.example.store.controller;

import com.example.store.dto.request.CategoryRequestDTO;
import com.example.store.dto.response.CategoryResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/category")
public class CategoryController {
    @Autowired private CategoryService categoryService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @GetMapping(value = "/{id}")
    public CategoryResponseDTO getCategoryById (@PathVariable(name = "id") Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> createCategory(@ModelAttribute @Valid CategoryRequestDTO categoryRequestDTO){
        return categoryService.createCategory(categoryRequestDTO);
    }

    @PutMapping(value = "")
    public ResponseEntity<ResponseObject> updateCategory(@ModelAttribute @Valid CategoryRequestDTO categoryRequestDTO, @RequestParam(name = "id") Long id){
        return categoryService.updateCategory(categoryRequestDTO, id);
    }

    @DeleteMapping(value ="")
    public ResponseEntity<ResponseObject> safeDelete(@RequestParam(name = "id") Long id){
        return categoryService.safeDelete(id);
    }

    @DeleteMapping(value = "remove")
    public ResponseEntity<ResponseObject> deleteCategory(@RequestParam(name = "categoryId") Long id) {
        return categoryService.deleteCategory(id);
    }
}
