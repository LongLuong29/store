package com.example.store.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
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

  @NotNull(message = "Order total price is required")
  private BigDecimal totalPrice;
  private BigDecimal shippingFee;
//  private BigDecimal voucherDiscount;
  private BigDecimal finalPrice;
  @Column(length = 333)
  private String note;
  @NotNull(message = "Payment method is required")
  private String paymentMethod;

  private String deliveryApartmentNumber;
  private String deliveryWard;
  private String deliveryDistrict;
  private String deliveryProvince;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date orderedDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date doneDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date deliveredDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date paidDate;
}
