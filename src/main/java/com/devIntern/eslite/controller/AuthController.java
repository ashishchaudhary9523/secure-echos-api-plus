package com.devIntern.eslite.controller;

import com.devIntern.eslite.Security.JWTTokenProvider;
import com.devIntern.eslite.model.Customer;
import com.devIntern.eslite.model.Vault;
import com.devIntern.eslite.payload.JWTAuthResponse;
import com.devIntern.eslite.payload.LoginDTO;
import com.devIntern.eslite.payload.SignupDTO;
import com.devIntern.eslite.repository.CustomerRepository;
import com.devIntern.eslite.service.implementation.EmailServiceImplementation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final EmailServiceImplementation  emailService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, CustomerRepository customerRepository, EmailServiceImplementation emailService, PasswordEncoder passwordEncoder, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.customerRepository = customerRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO){
        Customer customer = customerRepository.findById(loginDTO.getUserName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!customer.isVerified()) {
            return new ResponseEntity<>("Please verify your email before logging in.", HttpStatus.FORBIDDEN);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUserName() ,
                loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupDTO signupDTO) {
        if(customerRepository.existsCustomerByUserName(signupDTO.getUserName())){
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if(customerRepository.existsCustomerByEmail(signupDTO.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        Customer customer = new Customer();
        customer.setName(signupDTO.getName());
        customer.setEmail(signupDTO.getEmail());
        customer.setUserName(signupDTO.getUserName());
        customer.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
// Generate verification token
        String token = UUID.randomUUID().toString();
        customer.setVerificationToken(token);
        customer.setTokenExpiry(LocalDateTime.now().plusHours(24)); // 24h validity

        Vault vault = new Vault();
        vault.setCustomer(customer);
        vault.setEncryptedData("this is the dummy data");
        customer.setVault(vault);

        customerRepository.save(customer);

        // Send email
        emailService.sendVerificationEmail(customer.getEmail(), token);

        return new ResponseEntity<>("Successfully signed up! Please check your email for verification.", HttpStatus.OK);
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        Customer customer = (Customer) customerRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (customer.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Verification link expired", HttpStatus.BAD_REQUEST);
        }

        customer.setVerified(true);
        customer.setVerificationToken(null);
        customer.setTokenExpiry(null);

        customerRepository.save(customer);

        return new ResponseEntity<>("Email verified successfully! You can now log in.", HttpStatus.OK);
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@RequestParam("email") String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email not found"));

        if (customer.isVerified()) {
            return new ResponseEntity<>("Your email is already verified!", HttpStatus.BAD_REQUEST);
        }

        // Generate a new token & expiry
        String token = UUID.randomUUID().toString();
        customer.setVerificationToken(token);
        customer.setTokenExpiry(LocalDateTime.now().plusHours(24));

        customerRepository.save(customer);

        // Send verification email again
        emailService.sendVerificationEmail(customer.getEmail(), token);

        return new ResponseEntity<>("Verification email resent! Please check your inbox.", HttpStatus.OK);
    }



}
