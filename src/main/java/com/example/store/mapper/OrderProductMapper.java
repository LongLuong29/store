package com.example.store.mapper;


import com.example.store.dto.response.OrderProductResponseDTO;
import com.example.store.entities.OrderProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderProductMapper {

    @Mapping(target = "productId", source = "b.product.id")
    @Mapping(target = "productName", source = "b.product.name")
    @Mapping(target = "productImage", source = "b.product.thumbnail")
    @Mapping(target = "orderId", source = "b.order.id")
    @Mapping(target = "amount", source = "b.quantity")
    @Mapping(target = "paidDate", source = "b.order.paidDate")
    @Mapping(target = "payMethod", source = "b.order.paymentMethod")
    @Mapping(target = "status", source = "b.order.status")
    @Mapping(target = "price",  source = "b.order.totalPrice")
    OrderProductResponseDTO orderProductToOrderProductResponseDTO(OrderProduct b);
}
