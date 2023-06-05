package com.example.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordRequestDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
