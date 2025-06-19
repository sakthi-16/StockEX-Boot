package com.org.StockEX.service;

import com.org.StockEX.DTO.StocksDTO;
import com.org.StockEX.Entity.Havings;
import com.org.StockEX.Entity.Stocks;
import com.org.StockEX.repository.StocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class AddStockService {

    @Autowired
    private StocksRepo stocksRepo;

    @Autowired
    private Stocks stocks;

    @Autowired
    private Havings holdings;

    public ResponseEntity<?> addStocks(StocksDTO stocksDTO) {



        stocks.setStockName(stocksDTO.getStockName());
        stocks.setStocksImage(stocksDTO.getStocksImage());
        stocks.setStocksDeclared(stocksDTO.getStocksDeclared());
        stocks.setTotalStocks(stocksDTO.getTotalStocks());
        System.out.println(stocksDTO.getStocksDeclared()+" stocksDeclared = " + stocks.getStocksDeclared());
        System.out.println(stocksDTO.getTotalStocks()+" totalStocks = " + stocks.getTotalStocks());



        stocks.setStockPrice(stocks.getStocksDeclared().divide(BigDecimal.valueOf(stocks.getTotalStocks()),2, RoundingMode.HALF_UP));



        stocksRepo.save(stocks);

        return ResponseEntity.ok(Map.of("message","Stock Added Successfully"));

    }

}
