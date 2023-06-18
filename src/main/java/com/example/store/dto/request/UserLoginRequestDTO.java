package com.example.store.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserLoginRequestDTO {
    private String username;

    private String password;

    private int otp;
}
