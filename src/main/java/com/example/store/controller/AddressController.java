package com.example.store.controller;

import com.example.store.dto.request.AddressRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.AddressDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/address")
public class AddressController {
    @Autowired private AddressDetailService addressDetailService;

    @PostMapping(value = "/user")
    public ResponseEntity<ResponseObject> createAddressUser(@ModelAttribute AddressRequestDTO addressRequestDTO, @RequestParam(name = "userId") Long userId){
        return addressDetailService.createAddressDetail(addressRequestDTO, userId);
    }

    @PutMapping(value = "/user")
    public ResponseEntity<ResponseObject> updateAddressUser(@ModelAttribute AddressRequestDTO addressRequestDTO,
                                                            @RequestParam(name = "userId") Long userId,
                                                            @RequestParam(name = "addressId") Long addressId){
        return addressDetailService.updateAddressDetail(addressId, userId, addressRequestDTO);
    }

    @GetMapping(value = "/user")
    public ResponseEntity<?> getAddressByUser(@RequestParam(name = "userId") Long userId){
        return addressDetailService.getAddressByUser(userId);
    }

    @DeleteMapping(value = "/user")
    public ResponseEntity<ResponseObject> deleteAddressUser(@RequestParam(name = "userId") Long userId,
                                                            @RequestParam(name = "addressId") Long addressId){
        return addressDetailService.deleteAddressDetail(addressId, userId);
    }

    @DeleteMapping(value = "/user/softDelete")
    public ResponseEntity<ResponseObject> softDeleteAddressUser(@RequestParam(name = "userId") Long userId,
                                                            @RequestParam(name = "addressId") Long addressId,
                                                                @RequestParam(name = "deleted") boolean deleted){
        return addressDetailService.safeDeleteAddressDetail(addressId, userId, deleted);
    }
}
