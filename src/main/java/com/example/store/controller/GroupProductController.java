package com.example.store.controller;

import com.example.store.dto.request.GroupProductRequestDTO;
import com.example.store.dto.response.GroupProductResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.GroupProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/group-product")
public class GroupProductController {

    @Autowired private GroupProductService groupProductService;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllGroupProduct() {
        return groupProductService.getAllGroupProduct();
    }

    @PostMapping(value = "")
    public ResponseEntity<ResponseObject> createGroupProduct(@ModelAttribute @Valid GroupProductRequestDTO groupProductRequestDTO){
        return groupProductService.createGroupProduct(groupProductRequestDTO);
    }

    @PutMapping(value = "")
    public ResponseEntity<ResponseObject> updateGroupProduct(@ModelAttribute @Valid GroupProductRequestDTO groupProductRequestDTO, @RequestParam(name = "id") Long id){
        return groupProductService.updateGroupProduct(groupProductRequestDTO, id);
    }

    @DeleteMapping(value = "/softDelete")
    public ResponseEntity<ResponseObject> softDeleteGroupProduct(@RequestParam(name = "id") Long id,
                                                                 @RequestParam(name = "deleted") boolean deleted){
        return groupProductService.softDeleteGroupProduct(id,deleted);
    }

    @DeleteMapping(value = "")
    public ResponseEntity<ResponseObject> deleteGroupProduct(@RequestParam(name = "id") Long id){
        return groupProductService.deleteGroupProduct(id);
    }

    @GetMapping(value = "/{id}")
    public GroupProductResponseDTO getGroupProductById(@PathVariable(name = "id") Long id){
        return groupProductService.getGroupProductById(id);
    }

}
