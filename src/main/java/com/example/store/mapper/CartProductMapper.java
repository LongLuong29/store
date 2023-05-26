package com.example.store.mapper;

import com.example.store.dto.response.CartProductResponseDTO;
import com.example.store.entities.CartProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartProductMapper {
    @Mapping(target = "productId", source = "c.product.id")
    @Mapping(target = "cartId", source = "c.cart.id")
    @Mapping(target = "amount", source = "c.amount")
    @Mapping(target = "price", source = "c.product.price")
    @Mapping(target = "productName", source = "c.product.name")
    @Mapping(target = "productImage", source = "c.product.thumbnail")

    CartProductResponseDTO cartProductToCartProductResponseDTO(CartProduct c);
}
