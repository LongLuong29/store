package com.example.store.dto.response;

import com.example.store.entities.Address;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressDetailResponseDTO {
    private Long user;
    private Address address;
    private boolean defaultAddress;
    private boolean deleted;
}
