package com.org.StockEX.DTO;


import com.org.StockEX.Entity.Stocks;
import com.org.StockEX.Entity.UsersAccount;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmBuyStocksDTO {

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
