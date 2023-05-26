package com.example.store.services.implement;

import com.example.store.dto.request.BrandRequestDTO;
import com.example.store.dto.response.BrandResponseDTO;
import com.example.store.dto.response.ResponseObject;
import com.example.store.entities.Brand;
import com.example.store.exceptions.ResourceAlreadyExistsException;
import com.example.store.exceptions.ResourceNotFoundException;
import com.example.store.mapper.BrandMapper;
import com.example.store.repositories.BrandRepository;
import com.example.store.services.BrandService;
import jakarta.transaction.Transactional;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    private final BrandMapper mapper = Mappers.getMapper(BrandMapper.class);

    @Autowired private BrandRepository brandRepository;

    @Override
    public ResponseEntity<?> getAllBrand() {
        List<Brand> getBrandList = brandRepository.findAll();
        List<BrandResponseDTO> brandResponseDTOList = new ArrayList<>();
        for(Brand b: getBrandList){
            BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(b);
            brandResponseDTOList.add(brandResponseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(brandResponseDTOList);
    }

    @Override
    public ResponseEntity<ResponseObject> createBrand(BrandRequestDTO brandRequestDTO) {
        Brand brand = mapper.brandRequestDTOToBrand(brandRequestDTO);
        brand = checkExits(brand);
        brand.setStatus(true);
        Brand brandSaved = brandRepository.save(brand);
        BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(brandSaved);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Create brand successfully!", brandResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> updateBrand(BrandRequestDTO brandRequestDTO, Long id) {
        Brand brand = mapper.brandRequestDTOToBrand(brandRequestDTO);
        Brand getBrand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + id));
        brand.setId(id);
        brand = checkExits(brand);

        Brand branSaved = brandRepository.save(brand);
        BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(branSaved);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK, "Update brand successfully!", brandResponseDTO));
    }

    @Override
    public ResponseEntity<ResponseObject> deleteBrand(Long id) {
        Brand getBrand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + id));
        brandRepository.delete(getBrand);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ResponseObject(HttpStatus.OK,"Delete brand successfully!"));
    }

    @Override
    public BrandResponseDTO getBrandById(Long id) {
        Brand getBrand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Could not find brand with ID = " + id));
        BrandResponseDTO brandResponseDTO = mapper.brandToBrandResponseDTO(getBrand);
        return brandResponseDTO;
    }

    //check brand name exits or not
    private Brand checkExits(Brand brand){
        Optional<Brand> getBrand = brandRepository.findBrandByName(brand.getName());
        if(getBrand.isPresent()){
            if(brand.getId() == null){
                throw new ResourceAlreadyExistsException("Brand name already exists");
            } else {
                if (brand.getId() != getBrand.get().getId()) {
                    throw new ResourceAlreadyExistsException("Brand name already exists");
                }
            }
        }
        return brand;
    }
}
