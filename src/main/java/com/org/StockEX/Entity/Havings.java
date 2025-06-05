package com.org.StockEX.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Entity
@Data
@Table(
        name = "Havings",
        uniqueConstraints = @UniqueConstraint(columnNames = {"email", "stockName"})
)
@Component
@NoArgsConstructor
public class Havings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long holding;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String stockName;

    private String stockImage;

    private Integer holdingStocks;

    private BigDecimal oldStockPrice;

    private BigDecimal profit_loss_same;

    private BigDecimal avgStockPrice;


    @Column(name = "total_holdings_value", columnDefinition = "DECIMAL(10,2) DEFAULT 0.00")
    private BigDecimal totalHoldingsValue;

}
