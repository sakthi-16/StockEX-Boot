package com.org.StockEX.DTO;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;

}
