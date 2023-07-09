package com.example.store.dto.response;

import com.example.store.entities.Attribute;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductAttributeResponseDTO {
  private Long productId;
  private String name;
  private boolean deleted;
  private Attribute attribute;
}
