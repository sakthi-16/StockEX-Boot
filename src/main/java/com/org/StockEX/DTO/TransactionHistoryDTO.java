package com.org.StockEX.DTO;



import com.org.StockEX.repository.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;



public class TransactionHistoryDTO {


    private String stockName;
    private String stockImage;
    private BigDecimal amountCameInOrGoneOut;
    private String transactionStatus;


    public TransactionHistoryDTO(String stockName, String stockImage, BigDecimal amountCameInOrGoneOut) {
        this.stockName = stockName;
        this.stockImage = stockImage;
        this.amountCameInOrGoneOut = amountCameInOrGoneOut;
    }


    public String getStockName() { return stockName; }
    public String getStockImage() { return stockImage; }
    public BigDecimal getAmountCameInOrGoneOut() { return amountCameInOrGoneOut; }


}
