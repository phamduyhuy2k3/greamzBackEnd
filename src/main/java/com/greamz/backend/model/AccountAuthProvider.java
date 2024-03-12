package com.greamz.backend.model;

import com.greamz.backend.enumeration.AuthProvider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AccountAuthProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String providerId;
    @Enumerated
    private AuthProvider provider;
    @ManyToOne
    private AccountModel account;
}
