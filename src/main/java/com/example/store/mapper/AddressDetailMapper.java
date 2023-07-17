package com.example.store.mapper;

import com.example.store.dto.response.AddressDetailResponseDTO;
import com.example.store.entities.AddressDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressDetailMapper {
    @Mapping(target = "user", source = "add.user.id")
    @Mapping(target = "address", source = "add.address")
    @Mapping(target = "defaultAddress", source = "add.defaultAddress")
    @Mapping(target = "deleted", source = "add.deleted")
    AddressDetailResponseDTO addressDetailToAddressDetailResponseDTO(AddressDetail add);
}
