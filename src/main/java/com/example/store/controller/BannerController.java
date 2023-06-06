package com.example.store.controller;

import com.example.store.dto.request.BannerRequestDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.services.BannerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/v1/banner")
public class BannerController {
    @Autowired private BannerService bannerService;

    @GetMapping("")
    public ResponseEntity<?> getAllBanner (){ return bannerService.getAllBanner(); }

//    @GetMapping("")
//    public ResponseEntity<?> getBannerAvailable(){
//        return bannerService.getBannerAvailable();
//    }

    @PostMapping("")
    public ResponseEntity<?> createBanner(@ModelAttribute @Valid BannerRequestDTO bannerRequestDTO){
        return bannerService.createBanner(bannerRequestDTO);
    }

    @DeleteMapping("")
    public ResponseEntity<ResponseObject> deleteBanner(@RequestParam(name = "bannerId") Long bannerId){
        return bannerService.deleteBanner(bannerId);
    }
}
