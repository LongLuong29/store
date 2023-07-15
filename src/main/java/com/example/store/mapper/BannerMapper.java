package com.example.store.mapper;

import com.example.store.dto.request.BannerRequestDTO;
import com.example.store.dto.request.BrandRequestDTO;
import com.example.store.dto.response.BannerResponseDTO;
import com.example.store.dto.response.BrandResponseDTO;
import com.example.store.entities.Banner;
import com.example.store.entities.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "photoUrl", expression = "java(null)")
    @Mapping(target = "status", source = "dto.status")
    @Mapping(target = "description", source = "dto.description")
    @Mapping(target = "link", source = "dto.link")
    @Mapping(target = "startDate", source = "dto.startDate")
    @Mapping(target = "endDate", source = "dto.endDate")
    Banner bannerRequestDTOToBanner(BannerRequestDTO dto);

    @Mapping(target = "id",source ="banner.id" )
    @Mapping(target = "photoUrl", source ="banner.photoUrl" )
    @Mapping(target = "status", source = "banner.status")
    @Mapping(target = "description", source = "banner.description")
    @Mapping(target = "link", source = "banner.link")
    @Mapping(target = "startDate", source = "banner.startDate")
    @Mapping(target = "endDate", source = "banner.endDate")
    BannerResponseDTO bannerToBannerResponseDTO(Banner banner);
}
