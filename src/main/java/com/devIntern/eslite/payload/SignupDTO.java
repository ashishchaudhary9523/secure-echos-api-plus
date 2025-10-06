package com.devIntern.eslite.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupDTO {
    @NotEmpty
    @Size(min = 3 , max = 20 , message = "The name must be at least of 3 chars and at most 20 chars")
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 5 , max = 20 , message = "The username must be at least of 5 chars and at most 20 chars")
    private String userName;
    @NotEmpty
    @Size(min = 8 , max = 20 , message = "The password must be at least of 8 chars and at most 20 chars")
    private String password;
}
