package com.devIntern.eslite.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "customer" , uniqueConstraints = {@UniqueConstraint(columnNames = {"userName"}) ,
        @UniqueConstraint(columnNames = {"email"})})
public class Customer {

    private String name;
    @Id
    private String userName;
    @Column(unique = true , nullable = false)
    private String email;
    private String password;

    private boolean verified = false;

    @Column(name = "verification_token")
    private String verificationToken;

    @Column(name = "token_expiry")
    private LocalDateTime tokenExpiry;


    @OneToOne(mappedBy = "customer" , cascade = CascadeType.ALL , orphanRemoval = true)
    private Vault vault;

}