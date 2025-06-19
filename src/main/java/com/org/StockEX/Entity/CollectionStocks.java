package com.org.StockEX.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Collection_stocks")
public class CollectionStocks {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userCollectionStockId;


    @NotNull
    private String stockName;


    private BigDecimal stockPriceWhenAdded;


    @NotNull
    private Long collectionId;


    private String collectionName;


    @NotNull
    private Long userId;


    private String userMail;

    private boolean flag=true;




}
