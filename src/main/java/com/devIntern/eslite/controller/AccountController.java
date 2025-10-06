package com.devIntern.eslite.controller;

import com.devIntern.eslite.payload.LoginDTO;
import com.devIntern.eslite.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete(@Valid @RequestBody LoginDTO loginDTO) {
        try{
            String message = accountService.deleteVault(loginDTO.getUserName(), loginDTO.getPassword());
            return new ResponseEntity<>(message , HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
