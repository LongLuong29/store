package com.example.store.services;

import com.example.store.dto.request.BannerRequestDTO;
import com.example.store.dto.response.ResponseObject;
import org.springframework.http.ResponseEntity;

public interface BannerService {
    ResponseEntity<ResponseObject> createBanner(BannerRequestDTO bannerRequestDTO);
    ResponseEntity<ResponseObject> updateBanner(Long id, BannerRequestDTO bannerRequestDTO);
    ResponseEntity<ResponseObject> deleteBanner(Long id);
    ResponseEntity<?> getAllBanner();
    ResponseEntity<?> getAllAvailableBanner();

}
