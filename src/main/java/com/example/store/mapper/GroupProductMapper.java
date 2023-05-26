package com.example.store.mapper;

import com.example.store.dto.request.GroupProductRequestDTO;
import com.example.store.dto.response.GroupProductResponseDTO;
import com.example.store.entities.GroupProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupProductMapper {
    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "dto.name")
//    @Mapping(target = "status", source = "dto.status")
//    @Mapping(target = "image", source = "dto.image")
    GroupProduct groupProductRequestDTOToGroupProduct(GroupProductRequestDTO dto);

    @Mapping(target = "id",source ="groupProduct.id" )
    @Mapping(target = "name", source ="groupProduct.name" )
    @Mapping(target = "status", source = "groupProduct.status")
    GroupProductResponseDTO groupProductToGroupProductResponseDTO(GroupProduct groupProduct);
}
