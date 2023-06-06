package com.example.store.entities.Keys;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleKey implements Serializable {
    private Long user; //product
    private Long role; // cart

    @Override
    public int hashCode() {
        return Objects.hash(getRole(), getUser());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof UserRoleKey)) return false;
        UserRoleKey that = (UserRoleKey) obj;
        return getRole().equals(that.role) && getUser().equals(that.user);
    }
}
