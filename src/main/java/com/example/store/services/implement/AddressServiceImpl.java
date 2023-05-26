package com.example.store.services.implement;

import com.example.store.dto.request.AddressRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {
    @Override
    public ResponseEntity<ResponseObject> createAddress(AddressRequestDTO addressRequestDTO) {
        return null;
    }
}
