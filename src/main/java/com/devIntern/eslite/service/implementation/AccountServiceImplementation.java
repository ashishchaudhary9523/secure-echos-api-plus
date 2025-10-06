package com.devIntern.eslite.service.implementation;

import com.devIntern.eslite.Exceptions.SecureEchoAPIException;
import com.devIntern.eslite.model.Customer;
import com.devIntern.eslite.repository.CustomerRepository;
import com.devIntern.eslite.service.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImplementation implements AccountService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailServiceImplementation  emailServiceImplementation;

    public AccountServiceImplementation(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, EmailServiceImplementation emailServiceImplementation) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailServiceImplementation = emailServiceImplementation;
    }

    @Transactional
    @Override
    public String deleteVault(String userName, String password) throws Exception {
        String currentUserName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!userName.equals(currentUserName)){
            return "You are not authorized to access this resource. Please contact your administrator for further assistance.";
        }
        Customer customer = customerRepository.findById(userName)
                .orElseThrow(() -> new SecureEchoAPIException(HttpStatus.NOT_FOUND , "User not found"));
        if(passwordEncoder.matches(password , customer.getPassword())) {
            emailServiceImplementation.sendAccountDeletionEmail(customer.getEmail());
            customerRepository.delete(customerRepository.findByUserName(userName));
            return "The Account was deleted successfully";
        }
        return "The Account was not deleted";
    }
}
