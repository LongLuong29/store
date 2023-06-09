package com.example.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequestDTO {

    @NotNull(message = "Order total price is required")
    private BigDecimal totalPrice;
    private BigDecimal shippingFee;
    private BigDecimal finalPrice;
    private String status;
    @Column(length = 333)
    private String note;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    private Long addressId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date doneDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidDate;
}
