package com.org.StockEX.service;

import com.org.StockEX.Entity.ConfirmBuyStocks;
import com.org.StockEX.repository.ConfirmBuyStocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Service
public class ShowTransactionStatus {

    @Autowired
    private ConfirmBuyStocksRepo confirmBuyStocksRepo;

    public List<ConfirmBuyStocks>transactionStatus(String email){
        return   confirmBuyStocksRepo.findByUserMail(email);
    }
}
