package com.example.store.mapper;

import com.example.store.dto.request.OrderRequestDTO;
import com.example.store.dto.response.OrderResponseDTO;
import com.example.store.entities.Order;
import com.example.store.entities.ProductDiscount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "totalPrice", source = "b.totalPrice")
    @Mapping(target = "shippingFee", source = "b.shippingFee")
    @Mapping(target = "finalPrice", source = "b.finalPrice")
    @Mapping(target = "note", source = "b.note")
    @Mapping(target = "status", source = "b.status")
    @Mapping(target = "paymentMethod", source ="b.paymentMethod")
    @Mapping(target = "address", expression = "java(null)")
    @Mapping(target = "orderedDate", source ="b.orderedDate")
    @Mapping(target = "doneDate", source ="b.doneDate")
    @Mapping(target = "deliveredDate", source ="b.deliveredDate")
    @Mapping(target = "paidDate", source ="b.paidDate")
    Order orderRequestDTOToOrder(OrderRequestDTO b);

    @Mapping(target = "userName",source = "b.user.name")
    @Mapping(target = "orderId", source = "b.id")
    @Mapping(target = "status", source = "b.status")

    @Mapping(target = "totalPrice", source = "b.totalPrice")
    @Mapping(target = "shippingFee", source = "b.shippingFee")
    @Mapping(target = "finalPrice", source = "b.finalPrice")
//    @Mapping(target = "orderTotal", source = "b.orderTotal")
    @Mapping(target = "note", source = "b.note")
    @Mapping(target = "paymentMethod", source ="b.paymentMethod")

    @Mapping(target = "orderedDate", source ="b.orderedDate")
    @Mapping(target = "doneDate", source ="b.doneDate")
    @Mapping(target = "deliveredDate", source ="b.deliveredDate")
    @Mapping(target = "paidDate", source ="b.paidDate")

    @Mapping(target = "addressId", source = "b.address.id")
    @Mapping(target = "deliveryApartmentNumber", source = "b.address.apartmentNumber")
    @Mapping(target = "deliveryWard", source = "b.address.ward")
    @Mapping(target = "deliveryDistrict", source = "b.address.district")
    @Mapping(target = "deliveryProvince", source = "b.address.province")
    OrderResponseDTO orderToOrderResponseDTO(Order b);

}
