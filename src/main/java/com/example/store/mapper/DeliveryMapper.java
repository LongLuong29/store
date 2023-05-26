package com.example.store.mapper;

import com.example.store.dto.request.DeliveryRequestDTO;
import com.example.store.dto.response.DeliveryResponseDTO;
import com.example.store.entities.Delivery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    @Mapping(target = "status", source = "dto.status")
    @Mapping(target = "address", expression = "java(null)")
    @Mapping(target = "order", expression = "java(null)")
    @Mapping(target = "shipper", expression = "java(null)")
    Delivery deliveryRequestDTOToDelivery(DeliveryRequestDTO dto);



    @Mapping(target = "id", source = "d.id")
    @Mapping(target = "status", source = "d.status")
    @Mapping(target = "orderId", source = "d.order.id")
    @Mapping(target = "totalPrice", source = "d.order.totalPrice")
    @Mapping(target = "shipperId", source = "d.shipper.id")
    @Mapping(target = "shipperName", source = "d.shipper.name")
    @Mapping(target = "shipperPhone", source = "d.shipper.phone")
    @Mapping(target = "deliveryApartmentNumber", source = "d.address.apartmentNumber")
    @Mapping(target = "deliveryWard", source = "d.address.ward")
    @Mapping(target = "deliveryDistrict", source = "d.address.district")
    @Mapping(target = "deliveryProvince", source = "d.address.province")
    DeliveryResponseDTO deliveryToDeliveryResponseDTO(Delivery d);
}
