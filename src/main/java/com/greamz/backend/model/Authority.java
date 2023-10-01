package com.greamz.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.greamz.backend.enumeration.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table( uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account_id", "role"})
})
public class Authority implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne()
    @JsonBackReference
    private AccountModel account;
    @Enumerated(EnumType.STRING)
    private Role role;


}
