package com.example.store.dto.response;

import com.example.store.entities.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductAttributeResponseDTO {
  private Long productId;
  private String name;
  private Attribute attribute;
}
