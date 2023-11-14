package com.greamz.backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greamz.backend.common.TimeStampEntity;
import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.enumeration.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "Account")
@AllArgsConstructor
public class AccountModel extends TimeStampEntity  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    private String username;
    private String password;
    private String fullname;
    @Column(unique = true)
    private String email;
    private String photo;
    private boolean isEnabled;
    @Column(nullable = false)
    private Boolean emailVerified = false;
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    private String locale;
    @OneToMany
    private List<Voucher> vouchers;
    @OneToMany(mappedBy = "account")
    private List<Orders> orders;
    @OneToMany(mappedBy = "account")
    private List<Review> reviews;
    @OneToMany(mappedBy = "account")
    private List<Disscusion> disscusions;
    @Enumerated(EnumType.STRING)
    private Role role;


}
