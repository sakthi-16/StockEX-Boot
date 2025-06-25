package com.org.StockEX.service;


import com.org.StockEX.DTO.UpdateStockPriceDTO;
import com.org.StockEX.repository.StocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class UpdateStockPriceService {

    @Autowired
    private StocksRepo stocksRepo;

    public ResponseEntity<?> updateStockPrice(UpdateStockPriceDTO updateStockPriceDTO){

        if(updateStockPriceDTO.getStockPrice().compareTo(BigDecimal.ZERO)<0){
            return ResponseEntity.badRequest().body(Map.of("message","Update Quantity can't be negative."));
        }

        String stockName=updateStockPriceDTO.getStockName();
        BigDecimal amount=updateStockPriceDTO.getStockPrice();

        int updatedRows=stocksRepo.updateStockPrice(amount,stockName);

        return updatedRows>0
                        ?ResponseEntity.ok(Map.of("message",updateStockPriceDTO.getStockName()+" Stock price updated to ₹"+updateStockPriceDTO.getStockPrice()))
                        :ResponseEntity.badRequest().body(Map.of("message","Something went Wrong"));
    }
}
