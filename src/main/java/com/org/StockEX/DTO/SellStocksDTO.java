package com.org.StockEX.DTO;

import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SellStocksDTO {

    @NotBlank(message="This field can't be empty")
    private String StockName;

    @NotNull(message = "StockQuantity cant be null")
    @Min(value = 1)
    private Integer stockQuantity;

    @NotBlank(message="Process won't begin without PIN.")
    private String accountPIN;


}
