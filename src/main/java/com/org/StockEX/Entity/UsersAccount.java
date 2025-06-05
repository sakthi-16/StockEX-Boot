package com.org.StockEX.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Component
public class UsersAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AccountTableNo;

    private String userBankAccount;
    private BigDecimal userAccountBalance;
    private String accountPassword;


    @OneToOne
    @JoinColumn(name="userId")
    private UsersCredentials users;
}


