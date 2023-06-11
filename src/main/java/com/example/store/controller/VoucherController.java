package com.example.store.controller;

import com.example.store.dto.request.VoucherRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.UserVoucherService;
import com.example.store.services.VoucherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/v1/voucher")
public class VoucherController {
    @Autowired private VoucherService voucherService;
    @Autowired private UserVoucherService userVoucherService;

    //Voucher
    @GetMapping("")
    public ResponseEntity<?> getAllVoucher(){return voucherService.getAllVoucher(); }

    @PostMapping("")
    public ResponseEntity<ResponseObject> createVoucher(@ModelAttribute @Valid VoucherRequestDTO voucherRequestDTO){
        return voucherService.createVoucher(voucherRequestDTO);
    }

    @DeleteMapping("")
    ResponseEntity<ResponseObject> deleteVoucher(@RequestParam(name = "voucherId") Long voucherId){
        return voucherService.deleteVoucher(voucherId);
    }

    //User-voucher
    @GetMapping(value = "/user")
    public ResponseEntity<?> getAllUserVoucher(@RequestParam(name = "userId") Long userId){
        return userVoucherService.getAllUserVoucher(userId);
    }
    @PostMapping(value = "/user")
    public ResponseEntity<ResponseObject> createUserVoucher(@RequestParam(name = "userId") Long userId,
                                                            @RequestParam(name = "voucherId") Long voucherId){
        return userVoucherService.createUserVoucher(userId, voucherId);
    }
    @DeleteMapping(value = "/user")
    public ResponseEntity<ResponseObject> deleteUserVoucher(@RequestParam(name = "userId") Long userId,
                                                            @RequestParam(name = "voucherId") Long voucherId){
        return userVoucherService.deleteUserVoucher(userId, voucherId);
    }
    //ORDER USER-VOUCHER
    @GetMapping(value = "/user/order")
    public ResponseEntity<?> getUserVouchersForOrder(@RequestParam(name = "userId") Long userId,
                                                     @RequestParam(name = "orderId") Long orderId){
        return userVoucherService.getUserVouchersForOrder(userId,orderId);
    }
    @GetMapping(value = "/user/verify")
    public ResponseEntity<?> getUserVoucherByCode(@RequestParam(name = "userId") Long userId,
                                                  BigDecimal totalPrice,
                                                  String code){
        return userVoucherService.getUserVoucherByCode(userId,totalPrice,code);
    }

    @PostMapping(value = "/user/calculate")
    public BigDecimal calculateVoucherDiscount(@RequestParam(name = "userId") Long userId,
                                               @RequestParam(name = "voucherId") Long voucherId,
                                               BigDecimal totalPrice, BigDecimal shippingFee){
        return userVoucherService.calculateVoucherDiscount(userId, voucherId, totalPrice, shippingFee);
    }
}
