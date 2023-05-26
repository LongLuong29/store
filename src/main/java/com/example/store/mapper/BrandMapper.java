package com.example.store.mapper;

import com.example.store.dto.request.BrandRequestDTO;
import com.example.store.dto.response.BrandResponseDTO;
import com.example.store.entities.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BrandMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "image", source = "dto.image")
    @Mapping(target = "status", source = "dto.status")
    Brand brandRequestDTOToBrand(BrandRequestDTO dto);

    @Mapping(target = "id",source ="brand.id" )
    @Mapping(target = "name", source ="brand.name" )
    @Mapping(target = "image", source = "brand.image")
    @Mapping(target = "status", source = "brand.status")
    BrandResponseDTO brandToBrandResponseDTO(Brand brand);
}
