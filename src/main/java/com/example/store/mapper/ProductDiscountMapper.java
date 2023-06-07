package com.example.store.mapper;

import com.example.store.dto.response.ProductDiscountResponseDTO;
import com.example.store.entities.ProductDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductDiscountMapper {
  @Mapping(target = "id", source = "p.user.id")
  @Mapping(target = "name", source = "p.user.name")
  @Mapping(target = "discount", source = "p.discount")
  ProductDiscountResponseDTO productDiscountToProductDiscountResponseDTO(ProductDiscount p);
}
