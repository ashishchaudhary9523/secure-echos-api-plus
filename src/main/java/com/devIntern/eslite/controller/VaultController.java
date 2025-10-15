package com.devIntern.eslite.controller;


import com.devIntern.eslite.payload.AccessDTO;
import com.devIntern.eslite.payload.VaultDTO;
import com.devIntern.eslite.service.VaultService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/vault")
public class VaultController {

    private final VaultService vaultService;

    @Autowired
    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping("/create-vault")
    public ResponseEntity<?> storeData(@Valid @RequestBody VaultDTO vaultDTO){
        try {
            String response = vaultService.createVault(vaultDTO);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>( "Failed to store data" , HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/get-data")
    public ResponseEntity<?> getVault(@Valid @RequestBody AccessDTO accessDTO){
        String response = null;
        try {
            response = vaultService.getVault(accessDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response , HttpStatus.OK);

    }

    @PutMapping("/store-data")
    public ResponseEntity<?> putData(@Valid @RequestBody VaultDTO vaultDTO){
        try {
            String response = vaultService.addToVault(vaultDTO);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>( "Failed to store data" , HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-vault")
    public ResponseEntity<?> deleteVault(@Valid @RequestBody AccessDTO accessDTO){
        try {
            vaultService.deleteVault(accessDTO);
            return new ResponseEntity<>("Successfully deleted the vault", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage() , HttpStatus.BAD_REQUEST);
        }
    }



}
