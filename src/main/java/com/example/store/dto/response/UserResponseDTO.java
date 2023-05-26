package com.example.store.dto.response;

import com.example.store.entities.Rank;
import com.example.store.entities.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private boolean gender;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date birthday;
    private boolean status;
    private String image;
    private double point;

    private Role role;
    private Rank rank;

}
