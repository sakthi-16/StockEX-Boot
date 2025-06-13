package com.org.StockEX.service;

import com.org.StockEX.Entity.Stocks;
import com.org.StockEX.repository.StocksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowStocksService {
    @Autowired
    private StocksRepo stocksRepo;

    public List<Stocks> showStocks(){

        return stocksRepo.getAllStocks();
    }
}

