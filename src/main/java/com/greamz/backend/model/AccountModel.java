package com.greamz.backend.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.greamz.backend.common.TimeStampEntity;
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
public class AccountModel extends TimeStampEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String photo;
    private boolean isEnabled;
    @OneToMany
    private List<Voucher> vouchers;
    @OneToMany(mappedBy = "account")
    private List<Orders> orders;
    @OneToMany(mappedBy = "account")
    private List<Review> reviews;
    @OneToMany(mappedBy = "account")
    private List<Disscusion> disscusions;
    @JsonManagedReference
    @OneToMany(mappedBy = "account",cascade = {CascadeType.ALL},fetch = FetchType.EAGER)
    private List<Authority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
