package com.org.StockEX.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="confirm_buy_stocks")
public class ConfirmBuyStocks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionID;

    private String message;

    private String subMID;

    private String sellerOrderNo;

    private BigDecimal amountToPay;

    private BigDecimal subTotal;

    private BigDecimal gst;

    private BigDecimal exchangeCharges;

    private BigDecimal brokerage;

    private String stockName;

    private int stockQuantity;

    private String bankName;

    private Long userId;

    private String userMail;


    private String checkSum;

    private String bankCode;


    private String transactionStatus;
}
