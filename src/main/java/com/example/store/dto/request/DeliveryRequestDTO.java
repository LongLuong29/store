package com.example.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import jakarta.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryRequestDTO {
    private Long addressId;
    @NotNull(message = "Order is required")
    private Long orderId;
    private Long shipperId;
    private String orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payDate;
//    @NotNull(message = "Status of Delivery is required")
//    private String status;
}
