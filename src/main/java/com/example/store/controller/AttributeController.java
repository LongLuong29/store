package com.example.store.controller;

import com.example.store.dto.request.AttributeRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.ProductAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/attribute")
public class AttributeController {
  @Autowired
  private ProductAttributeService attributeProductService;

  @PostMapping(value = "/product")
  public ResponseEntity<ResponseObject> createAttributeProduct(@ModelAttribute @Valid AttributeRequestDTO attributeRequestDTO, @RequestParam(name = "productId") Long productId){
    return attributeProductService.createProductAttribute(productId, attributeRequestDTO);
  }

  @PutMapping(value = "/product")
  public ResponseEntity<ResponseObject> updateAttributeProduct(@ModelAttribute @Valid AttributeRequestDTO attributeRequestDTO,
      @RequestParam(name = "productId") Long productId,
      @RequestParam(name = "attributeId") Long attributeId){
    return attributeProductService.updateProductAttribute(productId, attributeId, attributeRequestDTO);
  }

  @GetMapping(value = "/product")
  public ResponseEntity<?> getAttributeByProduct(@RequestParam(name = "productId") Long productId){
    return attributeProductService.getProductAttributeByProduct(productId);
  }

  @DeleteMapping(value = "/product/softDeleted")
  public ResponseEntity<?> softDeletedProductAttribute(@RequestParam(name = "productId") Long productId,
                                                       @RequestParam(name = "deleted") boolean deleted){
    return attributeProductService.softDelete(productId,deleted);
  }
}
