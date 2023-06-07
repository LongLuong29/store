package com.example.store.dto.response;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class DeliveryResponseDTO {
    private Long id;
    private String status;
    private BigDecimal totalPrice;
    private Long orderId;
    private Long shipperId;
    private String shipperName;
    private String shipperPhone;
    private String deliveryApartmentNumber;
    private String deliveryWard;
    private String deliveryDistrict;
    private String deliveryProvince;
}
