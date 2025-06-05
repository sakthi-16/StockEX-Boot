package com.org.StockEX.Entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Data
@NoArgsConstructor
@Component
public class UsersAccountHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountTransaction;


    private String email;

    private BigDecimal currentAccountBalance;

    private BigDecimal amountTransacted;

    private String amountTransactedTime;


    public String getCurrentIndianTime() {

        ZonedDateTime indiaTime = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


        return indiaTime.format(formatter);
    }


}
