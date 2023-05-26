package com.example.store.dto.response;

import com.example.store.entities.Discount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  ProductDiscountResponseDTO {
  private String id;
  private String name;
  private Discount discount;
}
