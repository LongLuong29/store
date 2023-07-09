package com.example.store.services.implement;

import com.example.store.dto.request.BannerRequestDTO;
import com.example.store.dto.response.BannerResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Banner;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.BannerMapper;
import com.example.store.repositories.BannerRepository;
import com.example.store.services.BannerService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BannerServiceImpl implements BannerService {

    @Autowired private BannerRepository bannerRepository;
    @Autowired private FirebaseImageServiceImpl imageService;


    private final BannerMapper mapper = Mappers.getMapper(BannerMapper.class);
    @Override
    public ResponseEntity<ResponseObject> createBanner(BannerRequestDTO bannerRequestDTO){
        Banner banner = mapper.bannerRequestDTOToBanner(bannerRequestDTO);

        banner.setStatus(true);
        try {
            banner.setPhotoUrl(imageService.save(bannerRequestDTO.getPhotoUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Banner bannerSaved = bannerRepository.save(banner);
        BannerResponseDTO bannerResponseDTO = mapper.bannerToBannerResponseDTO(bannerSaved);
        bannerResponseDTO.setPhotoUrl(imageService.getImageUrl(bannerSaved.getPhotoUrl()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create banner successfully !!!", bannerResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateBanner(Long id, BannerRequestDTO bannerRequestDTO) {
        Banner getBanner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find banner with this id: "+id));
        Banner banner = mapper.bannerRequestDTOToBanner(bannerRequestDTO);


        banner.setId(id);
        banner.setStatus(true);
        banner.setCreatedDate(getBanner.getCreatedDate());
        try {
            imageService.delete(getBanner.getPhotoUrl());
            banner.setPhotoUrl(imageService.save(bannerRequestDTO.getPhotoUrl()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Banner bannerSaved = bannerRepository.save(banner);
        BannerResponseDTO bannerResponseDTO = mapper.bannerToBannerResponseDTO(bannerSaved);
        bannerResponseDTO.setPhotoUrl(imageService.getImageUrl(bannerSaved.getPhotoUrl()));

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update successfully", bannerResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not found banner with this id: " + id));
        try {
            imageService.delete(banner.getPhotoUrl());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        bannerRepository.delete(banner);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Delete banner successfully"));
    }

    @Override
    public ResponseEntity<?> getAllBanner() {
        List<Banner> bannerList = bannerRepository.findAll();
        List<BannerResponseDTO> bannerResponseDTOList = new ArrayList<>();
        for(Banner banner: bannerList){
            BannerResponseDTO bannerResponseDTO = mapper.bannerToBannerResponseDTO(banner);
            bannerResponseDTOList.add(bannerResponseDTO);
            if(banner.getEndDate().compareTo(new Date()) < 0 ){
                banner.setStatus(false);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(bannerResponseDTOList);
    }

}
