package com.example.store.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressRequestDTO {
    private String province;
    private String district;
    private String ward;
    private String apartmentNumber;
    private boolean defaultAddress;
}
