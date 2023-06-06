package com.example.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductResponseDTO {
    private Long productId;
    private String productName;
    private String productImage;

    private Long orderId;
    private int amount;
//    private String payMethod;
//    private String status;
    private BigDecimal price;
    private UserResponseDTO userResponseDTO;

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date orderedDate;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date doneDate;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date deliveredDate;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date paidDate;

}
