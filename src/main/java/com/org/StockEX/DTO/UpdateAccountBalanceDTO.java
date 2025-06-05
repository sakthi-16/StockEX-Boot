package com.org.StockEX.DTO;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class UpdateAccountBalanceDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long updationCount;

    private String email;

    @NotBlank
    private String accountPIN;

    @NotNull
    private BigDecimal amount;

}
