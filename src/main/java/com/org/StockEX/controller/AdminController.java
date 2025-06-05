package com.org.StockEX.controller;

import com.org.StockEX.DTO.StocksDTO;
import com.org.StockEX.DTO.UpdateStockPriceDTO;
import com.org.StockEX.Entity.Stocks;
import com.org.StockEX.service.AddStockService;
import com.org.StockEX.service.ShowStocksService;
import com.org.StockEX.service.UpdateStockPriceService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AddStockService addStocksService;

    @PostMapping("/addStocks")

    public ResponseEntity<?> addStocks(@Valid @RequestBody StocksDTO stocksDTO){

        log.info(String.valueOf(stocksDTO));

        if (stocksDTO.getStocksDeclared().compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.badRequest().body("Stocks declared must be greater than zero");
        }

        if (stocksDTO.getTotalStocks() != null && stocksDTO.getTotalStocks() == 0)
        {
            return ResponseEntity.badRequest().body("Total stocks must be greater than zero");
        }


        return addStocksService.addStocks(stocksDTO);

    }



    @Autowired
    private UpdateStockPriceService updateStockPriceService;

    @PatchMapping("/updateStock")

    public ResponseEntity<?> Updatestock(@RequestBody UpdateStockPriceDTO updateStockPriceDTO){

        return updateStockPriceService.updateStockPrice(updateStockPriceDTO);


    }


    @Autowired
    private ShowStocksService showStocks;

    private String currentUserEmail ;

    @GetMapping("/showStocks")
    public List<Stocks> showStocks(){

        return showStocks.showStocks();
    }

    @PostMapping("/logout")
    public String logout() {

        SecurityContextHolder.clearContext();


        return "Logged out successfully";
    }

}
