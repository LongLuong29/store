package com.example.store.entities.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDetailKey implements Serializable {
    private Long user;
    private Long address;

    @Override
    public int hashCode() { return Objects.hash(user, address); }

    @Override
    public boolean equals(Object o) {
        if (this == o)  return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressDetailKey that = (AddressDetailKey) o;
        return Objects.equals(user, that.user) && Objects.equals(address,
                that.address);
    }

}
