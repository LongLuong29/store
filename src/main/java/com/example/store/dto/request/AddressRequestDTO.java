package com.example.store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequestDTO {
    private String province;
    private String district;
    private String ward;
    private String apartmentNumber;
    private boolean defaultAddress;
}
