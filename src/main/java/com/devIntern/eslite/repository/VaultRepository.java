package com.devIntern.eslite.repository;


import com.devIntern.eslite.model.Vault;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VaultRepository extends JpaRepository<Vault, String> {
    Vault findByVaultId(String vaultId);
    @Modifying
    @Transactional
    @Query("DELETE FROM Vault v WHERE v.vaultId = :vaultId")
    void deleteByVaultId(@Param("vaultId") String vaultId);

}
