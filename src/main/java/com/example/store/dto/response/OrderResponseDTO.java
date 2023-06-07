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
public class OrderResponseDTO {
  private String userName;
  private Long orderId;
  private String status;
  private String paymentMethod;
  private BigDecimal finalPrice;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date orderedDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date doneDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date deliveredDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date paidDate;
}
