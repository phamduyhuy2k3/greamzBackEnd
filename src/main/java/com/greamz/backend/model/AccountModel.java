package com.greamz.backend.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "Account")
@AllArgsConstructor

public class AccountModel  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String photo;
    private boolean isEnabled;
    private boolean mfaEnabled;
    private String mfaSecret;
    @OneToMany
    private List<Voucher> vouchers;
    @OneToMany
    private List<Orders> orders;
    @OneToMany
    private List<Review> reviews;
    @OneToMany
    private List<Disscusion> disscusions;
    @JsonManagedReference
    @OneToMany(mappedBy = "account",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},fetch = FetchType.EAGER)
    private List<Authority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities.stream().map(authority -> authority.getRole()).toList();
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
        return true;
    }
}
