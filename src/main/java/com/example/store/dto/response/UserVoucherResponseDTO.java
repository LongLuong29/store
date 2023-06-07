package com.example.store.dto.response;

import com.example.store.entities.Voucher;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserVoucherResponseDTO {
  private String id;
  private String name;
  private Voucher voucher;
}
