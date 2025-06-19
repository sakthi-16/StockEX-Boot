package com.org.StockEX.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockAdditionToCollectionDTO {


    private String stockName;

    private BigDecimal stockPriceWhenAdded;

    private String collectionName;


}

