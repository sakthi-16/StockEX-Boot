package com.org.StockEX.DTO;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class StocksDTO {

    @NotBlank(message = "StockName cant be empty")
    private String stockName;

    @NotBlank(message = "Stock's image cant be empty")
    private String stocksImage;

    @NotNull(message="StocksDeclared cant be empty")
    private BigDecimal stocksDeclared;

    @NotNull(message="TotalStocks cant be active")
    private Integer  totalStocks;

    private BigDecimal stockPrice;

}
