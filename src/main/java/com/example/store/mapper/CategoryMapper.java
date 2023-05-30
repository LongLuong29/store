package com.example.store.mapper;

import com.example.store.dto.request.CategoryRequestDTO;
import com.example.store.dto.response.CategoryResponseDTO;
import com.example.store.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", expression = "java(null)")
    @Mapping(target = "name", source = "c.name")
    @Mapping(target = "groupProduct.id", source = "c.groupProduct")
    Category categoryRequestDTOToCategory(CategoryRequestDTO c);

    @Mapping(target = "id", source = "c.id")
    @Mapping(target = "name", source = "c.name")
    @Mapping(target = "groupProduct", source = "c.groupProduct.id")
    @Mapping(target = "status", source = "c.status")
    CategoryResponseDTO categoryToCategoryResponseDTO(Category c);
}
