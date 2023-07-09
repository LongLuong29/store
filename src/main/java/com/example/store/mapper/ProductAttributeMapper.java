package com.example.store.mapper;

import com.example.store.dto.response.ProductAttributeResponseDTO;
import com.example.store.entities.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductAttributeMapper {
  @Mapping(target = "productId", source = "a.product.id")
  @Mapping(target = "name", source = "a.product.name")
  @Mapping(target = "attribute", source = "a.attribute")
  @Mapping(target = "deleted", source = "a.deleted")

  ProductAttributeResponseDTO attributeToProductAttributeResponseDTO(ProductAttribute a);
}
