package com.org.StockEX.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.org.StockEX.validation.Age18Plus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
public class UsersCredentials {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


    private String username;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String pan;

    private Long mobile;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Age18Plus
    private LocalDate dob;

    private String password;

    private String role = "USER";

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL)
    private UsersAccount usersAccount;

    private String lastUsedPassword;


}
