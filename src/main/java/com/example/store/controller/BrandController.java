package com.example.store.controller;

import com.example.store.dto.request.BrandRequestDTO;
import com.example.store.dto.response.BrandResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/brand")
public class BrandController {

    @Autowired private BrandService brandService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllBrand() {
        return brandService.getAllBrand();
    }

    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> createBrand(@ModelAttribute @Valid BrandRequestDTO brandRequestDTO){
        return brandService.createBrand(brandRequestDTO);
    }

    @PutMapping(value="")
    public ResponseEntity<ResponseObject> updateBrand(@ModelAttribute @Valid BrandRequestDTO brandRequestDTO, @RequestParam(name = "id") Long id){
        return brandService.updateBrand(brandRequestDTO, id);
    }

    @DeleteMapping(value="")
    public ResponseEntity<ResponseObject> safeDeleteBrand(@RequestParam(name = "id") Long id,
                                                          @RequestParam(name = "deleted") boolean deleted){
        return brandService.safeDeleteBrand(id, deleted);
    }

    @DeleteMapping(value = "/remove")
    public ResponseEntity<ResponseObject> deleteBrand(@RequestParam(name = "id") Long id){
        return brandService.deleteBrand(id);
    }


    @GetMapping(value = "/{id}")
    public BrandResponseDTO getBrandById(@PathVariable(name = "id") Long id){
        return brandService.getBrandById(id);
    }

}
