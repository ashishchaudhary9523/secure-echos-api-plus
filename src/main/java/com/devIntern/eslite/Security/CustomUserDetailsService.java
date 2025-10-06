package com.devIntern.eslite.Security;


import com.devIntern.eslite.model.Customer;
import com.devIntern.eslite.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;
    @Autowired
    public CustomUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUserNameOrEmail(usernameOrEmail , usernameOrEmail).orElseThrow(
                () -> new UsernameNotFoundException("Username or Email not found")
        );

        return new User(customer.getUserName() , customer.getPassword() ,new ArrayList<>());
    }

}
