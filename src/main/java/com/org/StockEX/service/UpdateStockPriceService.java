package com.org.StockEX.service;


import com.org.StockEX.DTO.UpdateStockPriceDTO;
import com.org.StockEX.repository.StocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UpdateStockPriceService {

    @Autowired
    private StocksRepo stocksRepo;

    public ResponseEntity<?> updateStockPrice(UpdateStockPriceDTO updateStockPriceDTO){

        String stockName=updateStockPriceDTO.getStockName();
        BigDecimal amount=updateStockPriceDTO.getStockPrice();

        int updatedRows=stocksRepo.updateStockPrice(amount,stockName);

        return updatedRows>0
                        ?ResponseEntity.ok(updateStockPriceDTO.getStockName()+" Stock price updated to "+updateStockPriceDTO.getStockPrice())
                        :ResponseEntity.badRequest().body("Something went Wrong");
    }
}
