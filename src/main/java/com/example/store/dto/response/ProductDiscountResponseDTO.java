package com.example.store.dto.response;

import com.example.store.entities.Discount;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class  ProductDiscountResponseDTO {
  private String id;
  private String name;
  private boolean deleted;
  private Discount discount;
  private boolean flashSale;
  private Date startDate;
  private Date endDate;
}
