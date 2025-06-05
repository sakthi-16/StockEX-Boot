package com.org.StockEX.DTO;


import com.org.StockEX.validation.Age18Plus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
public class UserCredentialsDTO {
    @NotBlank(message = "username cant be empty")
    private String username;

    @Email
    @NotBlank(message = "Email cant be empty")
    @Column(unique = true,nullable = false)
    private String email;

    @NotBlank(message="PAN is required field")
    private String pan;

    @Min(value = 1000000000L, message = "Mobile must be 10 digits")
    @Max(value = 9999999999L, message = "Mobile must be 10 digits")
    private long mobile;

    @PastOrPresent(message = "Dont enter the future dates")
    private LocalDate dob;


    @Size(min=8,message="Minimum 8 characters needed.")
    private String Password;

    private String lastUsedPassword;


}
