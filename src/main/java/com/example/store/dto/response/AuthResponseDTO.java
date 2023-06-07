package com.example.store.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthResponseDTO {
  private String name;
  private String accessToken;
}
