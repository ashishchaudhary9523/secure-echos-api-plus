package com.devIntern.eslite.service;

import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    String deleteVault(String userName , String password) throws Exception;
}
