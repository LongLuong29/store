package com.example.store.mapper;

import com.example.store.dto.response.CartResponseDTO;
import com.example.store.entities.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userId", source = "c.user.id")
    @Mapping(target = "cartId", source = "c.id")

    CartResponseDTO cartToCartResponseDTO(Cart c);
}
