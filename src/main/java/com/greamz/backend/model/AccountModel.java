package com.greamz.backend.model;

import com.greamz.backend.common.AbstractAuditEntity;
import com.greamz.backend.enumeration.AuthProvider;
import com.greamz.backend.enumeration.Role;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@Table(name = "Account")
@AllArgsConstructor
public class AccountModel extends AbstractAuditEntity  {
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
    private Boolean emailVerified = false;
    private String locale;
    @OneToMany
    private List<Voucher> vouchers;
    @OneToMany(mappedBy = "account")
    private List<Orders> orders;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(columnDefinition = "DECIMAL(10, 2) DEFAULT 0.00")
    private BigDecimal balance = BigDecimal.ZERO;
    @OneToMany(mappedBy = "account",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountAuthProvider> authProviders;

}
