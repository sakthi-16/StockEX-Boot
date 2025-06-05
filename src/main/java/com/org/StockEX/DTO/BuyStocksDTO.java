package com.org.StockEX.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Data
@Component
public class BuyStocksDTO {

    private String email;

    @NotBlank(message="StockName cant be empty")
    private String stockName;

    @NotNull(message = "StockQuantity cant be null")
    @Min(value = 1)
    private Integer stockQuantity;


    @NotBlank(message="Payment won't begin without PIN.")
    private String accountPIN;

}
