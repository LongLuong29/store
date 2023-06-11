package com.example.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProductResponseDTO {
    private Long productId;
    private String productName;
    private String productImage;

    private Long orderId;
    private int amount;
//    private String payMethod;
//    private String status;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private UserResponseDTO userResponseDTO;

}
