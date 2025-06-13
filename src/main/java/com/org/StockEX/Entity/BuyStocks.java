package com.org.StockEX.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class BuyStocks {

    @Id
    private String stockName;

    private Integer stockQuantity;

    private String accountPIN;

    private String bankName;

    private String bankCode;



}
