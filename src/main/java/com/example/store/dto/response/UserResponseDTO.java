package com.example.store.dto.response;

import com.example.store.entities.Rank;
import com.example.store.entities.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
//    private String password;
    private boolean gender;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;
    private boolean status;
    private boolean deleted;
    private String image;
    private double point;
    private BigDecimal wallet;

    private Role role;
    private Rank rank;

}
