package com.org.StockEX.DTO;

import com.org.StockEX.Entity.UsersCredentials;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Data
public class UserAccountDTO {



    @NotBlank
    private String userMail;

    @NotBlank
    private String userBankAccount;

    @NotNull
    private BigDecimal userAccountBalance;

    @NotBlank
    private String accountPassword;

    private UsersCredentials users;

}
