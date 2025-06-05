package com.org.StockEX.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserAccountHistoryDTO {
    private BigDecimal currentAccountBalance;
    private BigDecimal amountTransacted;
    private LocalDateTime amountTransactedTime;
}