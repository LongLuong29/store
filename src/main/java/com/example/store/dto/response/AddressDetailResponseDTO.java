package com.example.store.dto.response;

import com.example.store.entities.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AddressDetailResponseDTO {
    private Long user;
    private Address address;
    private boolean defaultAddress;
}
