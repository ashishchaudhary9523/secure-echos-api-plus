package com.devIntern.eslite.service;

import com.devIntern.eslite.payload.AccessDTO;
import com.devIntern.eslite.payload.VaultDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface VaultService {
    String createVault(@Valid VaultDTO vaultDTO) throws Exception;
    String getVault(@Valid AccessDTO accessDTO) throws Exception;
    String addToVault(VaultDTO vaultDTO) throws Exception;
    void deleteVault(AccessDTO accessDTO) throws Exception;
}
