package com.example.store.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private String status;
    private String paymentMethod;
    private BigDecimal totalPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderedDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date doneDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date paidDate;
}
