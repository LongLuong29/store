package com.example.store.dto.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class DeliveryResponseDTO {
    private Long id;
    private boolean deliveryStatus;
    private String status;
    private BigDecimal totalPrice;
    private String image;
    private Long orderId;
    private String userName;
    private String userPhone;
    private Long shipperId;
    private String shipperName;
    private String shipperPhone;
    private String deliveryApartmentNumber;
    private String deliveryWard;
    private String deliveryDistrict;
    private String deliveryProvince;
}
