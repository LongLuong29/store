package com.example.store.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressRequestDTO {
    private String province;
    private int provinceId;
    private String district;
    private int districtId;
    private String ward;
    private int wardId;
    private String apartmentNumber;
    private boolean defaultAddress;
}
