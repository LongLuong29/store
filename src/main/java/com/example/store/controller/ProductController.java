package com.example.store.controller;

import com.example.store.dto.request.ProductRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.ProductService;
import com.example.store.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/product")
public class  ProductController {
  @Autowired private ProductService productService;

  @GetMapping(value = "")
  public ResponseEntity<?> getAllProductOnTrading(
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER) int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE) int size){
    Pageable pageable = PageRequest.of(page - 1, size);
    return productService.getAllProductOnTrading(pageable);
  }

  @PostMapping(value = "")
  public ResponseEntity<ResponseObject> createProduct(@ModelAttribute @Valid ProductRequestDTO productRequestDTO){
    return productService.createProduct(productRequestDTO);
  }

  @PutMapping(value = "")
  public ResponseEntity<ResponseObject> updateProduct(@ModelAttribute @Valid ProductRequestDTO productRequestDTO,
      @RequestParam(name = "id") Long id){
    return productService.updateProduct(productRequestDTO, id);
  }

  @DeleteMapping(value = "")
  public ResponseEntity<ResponseObject> deleteProduct(@RequestParam(name = "id") Long id){
    return productService.deleteProduct(id);
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getProductById(@PathVariable(name = "id") Long id){
    return productService.getProductById(id);
  }

    @GetMapping(value = "/search")
  public ResponseEntity<?> findAll(@RequestParam(name = "search") String search,
      @RequestParam(name = "page", required = false, defaultValue = Utils.DEFAULT_PAGE_NUMBER)
      int page,
      @RequestParam(name = "size", required = false, defaultValue = Utils.DEFAULT_PAGE_SIZE)
      int size) {
    return this.productService.search(search, page, size);
  }
}
