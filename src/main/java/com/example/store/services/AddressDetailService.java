package com.example.store.services;

import com.example.store.dto.request.AddressRequestDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface AddressDetailService {
    ResponseEntity<ResponseObject> createAddressDetail(
            AddressRequestDTO addressRequestDTO,
            Long userId
    );
    ResponseEntity<ResponseObject> safeDeleteAddressDetail(Long addressId, Long userId, boolean deleted);
    ResponseEntity<?> getAddressByUser(Long id);

    ResponseEntity<ResponseObject> updateAddressDetail(Long addressId, Long userId, AddressRequestDTO addressRequestDTO);

    ResponseEntity<ResponseObject> deleteAddressDetail(Long addressId, Long userId);
}
