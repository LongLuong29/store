package com.example.store.mapper;

import com.example.store.dto.response.WishListResponseDTO;
import com.example.store.entities.WishList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WishListMapper {
    @Mapping(target = "userId", source = "c.user.id")
    @Mapping(target = "productId", source = "c.product.id")
    @Mapping(target = "productName", source = "c.product.name")
    @Mapping(target = "productImage", source = "c.product.thumbnail")
    @Mapping(target = "price", source = "c.product.price")
    WishListResponseDTO wishListToWishListResponseDTO(WishList c);
}
