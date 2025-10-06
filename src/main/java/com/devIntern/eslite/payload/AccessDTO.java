package com.devIntern.eslite.payload;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AccessDTO {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String key;
}
