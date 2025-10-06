package com.devIntern.eslite.service.implementation;


import com.devIntern.eslite.AESUtil.AESUtil;
import com.devIntern.eslite.Exceptions.SecureEchoAPIException;
import com.devIntern.eslite.model.Customer;
import com.devIntern.eslite.model.Vault;
import com.devIntern.eslite.payload.VaultDTO;
import com.devIntern.eslite.repository.CustomerRepository;
import com.devIntern.eslite.repository.VaultRepository;
import com.devIntern.eslite.service.VaultService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class VaultServiceImplementation implements VaultService {

    private final VaultRepository vaultRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;

    @Autowired
    public VaultServiceImplementation(VaultRepository vaultRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.vaultRepository = vaultRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    @Override
    public String storeVault(VaultDTO vaultDTO) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!vaultDTO.getUserName().equals(currentUserName)) {
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }

        if (!vaultRepository.existsById(vaultDTO.getUserName())) {
            return "User not found. Maybe you need to sign-up first?";
        }
        Vault vault = vaultRepository.findByUserName(vaultDTO.getUserName());
        if (vaultRepository.existsById(vaultDTO.getUserName()) && vault.getIsUpdated()) {
            return vaultDTO.getUserName() + "'s vault already exists. Cannot create a new one. Please contact your administrator for further assistance.";
        }
        LocalDateTime now = LocalDateTime.now();
        String encrypted = AESUtil.encrypt(now + "\n\n"+vaultDTO.getEncryptedData(), vaultDTO.getKey());
        String keyHash = passwordEncoder.encode(vaultDTO.getKey());

        vault.setEncryptedData(encrypted);
        vault.setKey(keyHash);
        vault.setIsUpdated(true);

        vaultRepository.save(vault);
        return "Data saved";
    }

    @Transactional
    @Override
    public String getVault(String userName, String key) {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!userName.equals(currentUserName)) {
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }
        Customer customer = customerRepository.findById(userName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        Vault vault = customer.getVault();
        if (passwordEncoder.matches(key, vault.getKey())) {
            String data = null;
            try {
                data = AESUtil.decrypt(vault.getEncryptedData(), key);
            } catch (Exception e) {
                return "Failed to decrypt data" + e.getMessage();
            }
            return data;
        } else {
            return "The key is incorrect. Please try again.";
        }
    }

    @Override
    public String addToVault(VaultDTO vaultDTO) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!vaultDTO.getUserName().equals(currentUserName)) {
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }

        if (!vaultRepository.existsById(vaultDTO.getUserName())) {
            return "User not found. Maybe you need to sign-up first?";
        }
        Vault vault = vaultRepository.findByUserName(vaultDTO.getUserName());
        if (vaultRepository.existsById(vaultDTO.getUserName()) && vault.getIsUpdated()) {
            if(passwordEncoder.matches(vaultDTO.getKey(), vault.getKey())) {

                LocalDateTime now = LocalDateTime.now();

                // Decrypt existing data (if needed)
                String existingEncryptedData = vault.getEncryptedData();
                String decryptedExistingData = AESUtil.decrypt(existingEncryptedData, vaultDTO.getKey());

                // Append new data
                String combinedData = decryptedExistingData + "\n\n" + now + "\n\n" + vaultDTO.getEncryptedData();
                String encrypted = AESUtil.encrypt(combinedData, vaultDTO.getKey());
                String keyHash = passwordEncoder.encode(vaultDTO.getKey());

                vault.setEncryptedData(encrypted);
                vault.setKey(keyHash);
                vault.setIsUpdated(true);

                vaultRepository.save(vault);
                return "Data saved";
            }else {
                return "The key is incorrect. Please try again.";
            }
        }

        return "Data was not added due to some issue.";
    }




}
