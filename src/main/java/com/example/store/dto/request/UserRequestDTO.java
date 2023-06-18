package com.example.store.dto.request;

import com.example.store.entities.Rank;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequestDTO {
    @NotNull(message="User name is required!")
    private String name;

    @NotNull(message="Email is required!")
    @Size(message="Invalid size.", max = 40, min=10)
    @Pattern(regexp=("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
            + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$"), message = "Invalid email")
    private String email;
    private boolean gender;
    private String phone;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
    private String password;
    private boolean status;
    MultipartFile image;
//    private double point;

    private Long role;
//    private Long rank;
}
