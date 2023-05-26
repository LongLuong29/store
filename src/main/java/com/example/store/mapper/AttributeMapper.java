package com.example.store.mapper;

import com.example.store.dto.request.AttributeRequestDTO;
import com.example.store.entities.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeMapper {
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "name", source = "dto.name")
  @Mapping(target = "value", source = "dto.value")
  Attribute attributeRequestDTOtoAttribute(AttributeRequestDTO dto);
}
