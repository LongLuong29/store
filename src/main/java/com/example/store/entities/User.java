package com.example.store.entities;

import com.example.store.entities.Enum.AuthenticationProvider;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, unique = true)
    @Pattern(regexp = ("^(?=.{1,64}@)[A-Za-z0-9\\+_-]+(\\.[A-Za-z0-9\\+_-]+)*@"
            + "[^-][A-Za-z0-9\\+-]+(\\.[A-Za-z0-9\\+-]+)*(\\.[A-Za-z]{2,})$"), message = "Invalid email")
    @Size(max =110, min = 10, message = "Invalid mail size")
    private String email;

    // custom trường data trong entity
    @Column(name = "name", length = 45)
    @NotNull(message = "Name is required")
    private String name;

    @Column(name = "gender")
    private boolean gender;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(unique = true)
    @Size(max = 12, min = 9, message = "Invalid phone size")
    private String phone;

    @NotNull(message = "Password is required")
    private String password;

    @NotNull(message = "Status is required")
    private boolean status;

    @Column(name = "images", length = 255)
    private String image;

    @Column(name = "point")
    private double point;

    @Column(length = 64)
    private String verificationCode;

    @ManyToOne(optional = true)
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne(optional = true)
    @JoinColumn(name = "rank_id")
    private Rank rank;

    @CreationTimestamp
    private Date createDate;

    @UpdateTimestamp
    private Date updateDate;

    /*
    @CreatedBy
    private User user;
    * */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST })
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Order> orders;

    @PreRemove
    public void deleteUser(){
        this.getOrders().forEach(order -> order.setUser(null));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities =new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

}
