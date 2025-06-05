package com.org.StockEX.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Data
@NoArgsConstructor
public class MyStockDisplayDTO {
    private String stockName;
    private String stockImage;
    private Integer holdingStocks;
    private BigDecimal avgStockPrice;
    private BigDecimal totalHoldingsValue;
    private BigDecimal currentStockPrice;
    private String growthStatus;
    private BigDecimal growthPercentage;

    public MyStockDisplayDTO(String stockName, String stockImage, Integer holdingStocks,
                             BigDecimal avgStockPrice, BigDecimal totalHoldingsValue,
                             BigDecimal currentStockPrice, String growthStatus) {
        this.stockName = stockName;
        this.stockImage = stockImage;
        this.holdingStocks = holdingStocks;
        this.avgStockPrice = avgStockPrice;
        this.totalHoldingsValue = totalHoldingsValue;
        this.currentStockPrice = currentStockPrice;
        this.growthStatus = growthStatus;
        this.growthPercentage = (currentStockPrice.subtract(avgStockPrice))
                .divide(avgStockPrice, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        System.out.println("\n"+growthPercentage);
    }


}
