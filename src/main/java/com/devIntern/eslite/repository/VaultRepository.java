package com.devIntern.eslite.repository;


import com.devIntern.eslite.model.Vault;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VaultRepository extends JpaRepository<Vault, String> {
        void deleteByUserName(String userName);
        Vault findByUserName(String userName);

}
