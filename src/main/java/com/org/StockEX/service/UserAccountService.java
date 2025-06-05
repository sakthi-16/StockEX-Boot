package com.org.StockEX.service;

import com.org.StockEX.DTO.UserAccountDTO;
import com.org.StockEX.Entity.UsersAccount;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.UserAccountRepo;
import com.org.StockEX.repository.Usercredentialsrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class UserAccountService {


    @Autowired
    private Usercredentialsrepo users;


    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersAccountHistory userAccountHistory;

    public ResponseEntity<String> addAccount(UserAccountDTO userAccountDTO) {

        Optional<UsersCredentials> optionalUser = users.findByEmail(userAccountDTO.getUserMail());


        if (optionalUser.isPresent()) {
            UsersCredentials user = optionalUser.get();

            UsersAccount userAccount = new UsersAccount();
            userAccount.setUserBankAccount(userAccountDTO.getUserBankAccount());
            userAccount.setUserAccountBalance(userAccountDTO.getUserAccountBalance());
            userAccountHistory.setCurrentAccountBalance(userAccountDTO.getUserAccountBalance());
            userAccount.setAccountPassword(userAccountDTO.getAccountPassword());

            userAccount.setUsers(user);

            userAccountRepo.save(userAccount);

            return ResponseEntity.ok(" Account addition successful");
        } else {

            return ResponseEntity.badRequest().body("Please use the same email , enter the one you used while registering.");
        }
    }

}
