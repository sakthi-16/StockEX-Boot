package com.org.StockEX.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stocks")
public class Stocks {



    @Id
    private String stockName;

    private String stocksImage;

    private BigDecimal stocksDeclared;

    private Integer totalStocks;

    private BigDecimal stockPrice;


}
