package com.devIntern.eslite.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "vault")
public class Vault {

    @Id()
    @Column(unique = true , nullable = false)
    private String vaultId;

//    private String userName;

    @Lob
    private String encryptedData;

    private String key;
    private Boolean isUpdated = false;

    @ManyToOne
    @JoinColumn(name = "user_name")
    private Customer customer;
}
