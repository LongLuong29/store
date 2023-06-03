package com.example.store.mapper;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.OrderResponseDTO;
import com.example.store.entities.Order;
import com.example.store.entities.ProductDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userName",source = "b.user.name")
    @Mapping(target = "orderId", source = "b.id")
    @Mapping(target = "status", source = "b.status")
    @Mapping(target = "paymentMethod", source = "b.paymentMethod")
    @Mapping(target = "finalPrice", source = "b.totalPrice")

    @Mapping(target = "orderedDate", source ="b.orderedDate")
    @Mapping(target = "doneDate", source ="b.doneDate")
    @Mapping(target = "deliveredDate", source ="b.deliveredDate")
    @Mapping(target = "paidDate", source ="b.paidDate")
    OrderResponseDTO orderToOrderResponseDTO(Order b);

    @Mapping(target = "status", source ="b.status")
    @Mapping(target = "paymentMethod", source ="b.paymentMethod")
    @Mapping(target = "totalPrice", source = "b.totalPrice")

    @Mapping(target = "orderedDate", source ="b.orderedDate")
    @Mapping(target = "doneDate", source ="b.doneDate")
    @Mapping(target = "deliveredDate", source ="b.deliveredDate")
    @Mapping(target = "paidDate", source ="b.paidDate")
    Order orderRequestDTOToOrder(OrderRequestDTO b);
}
