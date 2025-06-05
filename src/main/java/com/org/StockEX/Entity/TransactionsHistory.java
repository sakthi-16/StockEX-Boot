package com.org.StockEX.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Component
@Data
@NoArgsConstructor
public class TransactionsHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer transaction;


    private String email;

    private String stockName;

    private String stockImage;

    private BigDecimal amountCameInOrGoneOut;

    private String transactionType;

}
