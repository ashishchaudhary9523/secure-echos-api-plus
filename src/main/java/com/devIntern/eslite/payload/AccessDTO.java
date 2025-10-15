package com.devIntern.eslite.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessDTO {
    @NotEmpty
    private String vaultId;
    @NotEmpty
    @Pattern(
            regexp = "^[\\p{L}\\p{N}\\p{P}\\p{Z}]*$",
            message = "Vault name must not contain emojis or unsupported characters"
    )
    private String userName;
    @NotEmpty
    private String key;
}
