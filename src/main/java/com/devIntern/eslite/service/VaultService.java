package com.devIntern.eslite.service;

import com.devIntern.eslite.payload.VaultDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface VaultService {
    String storeVault(@Valid VaultDTO vaultDTO) throws Exception;
    String getVault(String userName , String key) throws Exception;
    public String addToVault(VaultDTO vaultDTO) throws Exception;
}
