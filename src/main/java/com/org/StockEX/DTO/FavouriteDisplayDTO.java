package com.org.StockEX.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavouriteDisplayDTO {

    private String collectionName;

    private String stockImageUrl;

    private String stockName;

    private Integer stockQuantity;

    private BigDecimal stockPriceWhenAdded;

    private BigDecimal currentStockPrice;

    private Double growthRate;


}
