package com.org.StockEX.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
public class UpdateStock {

    @Id
    private String stockName;

    @NotNull(message = "Dont leave this empty!")
    private BigDecimal stockPrice;

}
