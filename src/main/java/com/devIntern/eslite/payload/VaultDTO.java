package com.devIntern.eslite.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaultDTO {
    @NotEmpty
    @Size(min = 4, max = 20 , message = "The vault id must be within range of 4-20")
    @Pattern(
            regexp = "^[\\p{L}\\p{N}\\p{P}\\p{Z}]*$",
            message = "Vault name must not contain emojis or unsupported characters"
    )
    private String vaultId;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String encryptedData;
    @NotEmpty
    @Size(min = 16 , max = 16 , message = "The key must be of 16 chars")
    private String key;

    private int failedAttempts = 0;
}
