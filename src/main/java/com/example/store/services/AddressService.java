package com.example.store.services;

import com.example.store.dto.request.AddressRequestDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface AddressService {
    ResponseEntity<ResponseObject> createAddress(AddressRequestDTO addressRequestDTO);
}
