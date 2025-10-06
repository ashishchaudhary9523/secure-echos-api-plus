package com.devIntern.eslite.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDTO {
    @NotEmpty
    @Size(min = 5 , max = 20 , message = "The username must be at least of 5 chars and at most 20 chars")
    private String userName;
    @NotEmpty
    @Size(min = 8 , max = 20 , message = "The password must be at least of 8 chars and at most 20 chars")
    private String password;
}
