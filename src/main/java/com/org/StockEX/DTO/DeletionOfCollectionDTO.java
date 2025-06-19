package com.org.StockEX.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeletionOfCollectionDTO {

    private String stockName;


    private String collectionName;

}
