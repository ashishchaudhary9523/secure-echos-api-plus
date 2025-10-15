package com.devIntern.eslite.service.implementation;


import com.devIntern.eslite.AESUtil.AESUtil;
import com.devIntern.eslite.Exceptions.SecureEchoAPIException;
import com.devIntern.eslite.model.Customer;
import com.devIntern.eslite.model.Vault;
import com.devIntern.eslite.payload.AccessDTO;
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
    public String createVault(VaultDTO vaultDTO) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        Customer customer = customerRepository.findById(vaultDTO.getUserName())
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return "This user is not verified";
        }
        if (!vaultDTO.getUserName().equals(currentUserName)) {
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }
        if (vaultRepository.existsById(vaultDTO.getVaultId())) {
            return "The vault with id " + vaultDTO.getVaultId() + " already exists. Try with different ID.";
        }

        Vault vault = vaultRepository.findByVaultId(vaultDTO.getVaultId());
        if (vaultRepository.existsById(vaultDTO.getVaultId()) && vault.getIsUpdated()) {
            return "This vault has been created. Try another url to add more data.";
        }
        Vault newVault = new Vault();
        newVault.setVaultId(vaultDTO.getVaultId());
//        newVault.setUserName(vaultDTO.getUserName());
        newVault.setCustomer(customer);

        LocalDateTime now = LocalDateTime.now();
        String encrypted = AESUtil.encrypt(now + "\n" + vaultDTO.getEncryptedData() + "\n", vaultDTO.getKey());
        String keyHash = passwordEncoder.encode(vaultDTO.getKey());

        newVault.setEncryptedData(encrypted);
        newVault.setKey(keyHash);
        newVault.setIsUpdated(true);

        vaultRepository.save(newVault);
        return "Data saved";
    }


    @Transactional
    @Override
    public String getVault(AccessDTO accessDTO) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!accessDTO.getUserName().equals(currentUserName)) {
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }
        Customer customer = customerRepository.findById(accessDTO.getUserName())
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return "This user is not verified";
        }
        Vault vault = vaultRepository.findByVaultId(accessDTO.getVaultId());
        if (!vault.getCustomer().equals(customer)) {
            return "This vault does not belong to this customer";
        }
        if (passwordEncoder.matches(accessDTO.getKey(), vault.getKey())) {
            String data = null;
            try {
                data = AESUtil.decrypt(vault.getEncryptedData(), accessDTO.getKey());
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
        Customer customer = customerRepository.findById(vaultDTO.getUserName())
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return "This user is not verified";
        }
        if (!vaultDTO.getUserName().equals(currentUserName)) {
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }
        if (!vaultRepository.existsById(vaultDTO.getVaultId())) {
            return "Vault not found. Maybe you need to create one first?";
        }
        Vault vault = vaultRepository.findByVaultId(vaultDTO.getVaultId());
        if (vaultRepository.existsById(vaultDTO.getVaultId()) && vault.getIsUpdated()) {
            if (passwordEncoder.matches(vaultDTO.getKey(), vault.getKey())) {

                LocalDateTime now = LocalDateTime.now();

                // Decrypt existing data (if needed)
                String existingEncryptedData = vault.getEncryptedData();
                String decryptedExistingData = AESUtil.decrypt(existingEncryptedData, vaultDTO.getKey());

                // Append new data
                String combinedData = decryptedExistingData + "\n" + now + "\n" + vaultDTO.getEncryptedData();
                String encrypted = AESUtil.encrypt(combinedData, vaultDTO.getKey());
                String keyHash = passwordEncoder.encode(vaultDTO.getKey());

                vault.setEncryptedData(encrypted);
                vault.setKey(keyHash);
//                vault.setIsUpdated(true);

                vaultRepository.save(vault);
                return "Data saved";
            } else {
                return "The key is incorrect. Please try again.";
            }
        }

        return "Data was not added due to some issue.";
    }

    @Transactional
    @Override
    public void deleteVault(AccessDTO accessDTO) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!accessDTO.getUserName().equals(currentUserName)) {
            return;
        }
        Customer customer = customerRepository.findById(accessDTO.getUserName())
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND, "User not found"));
        if (!customer.isVerified()) {
            return;
        }
        Vault vault = vaultRepository.findByVaultId(accessDTO.getVaultId());
        if (!vault.getCustomer().equals(customer)) {
            return;
        }
        if(passwordEncoder.matches(accessDTO.getKey(), vault.getKey())){
            vaultRepository.deleteByVaultId(accessDTO.getVaultId());
        }
    }


}
