package com.org.StockEX.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyProfileDTO {

    private String userName;
    private Long userId;
    private String email;
    private String bankName;
    private BigDecimal currentBalance;
    private String accountPIN;
}
