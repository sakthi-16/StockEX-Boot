package com.org.StockEX.service;

import com.org.StockEX.DTO.UserAccountDTO;
import com.org.StockEX.Entity.UsersAccount;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.UserAccountRepo;
import com.org.StockEX.repository.Usercredentialsrepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service

public class UserAccountService {


    @Autowired
    private Usercredentialsrepo users;


    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;



    public ResponseEntity<?> addAccount(UserAccountDTO userAccountDTO) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();



        if (currentUserEmail.equals(userAccountDTO.getUserMail())) {
            Optional<UsersCredentials> optionalUser = users.findByEmail(userAccountDTO.getUserMail());

            UsersCredentials user = optionalUser.get();

            UsersAccount userAccount = new UsersAccount();
            userAccount.setUserBankAccount(userAccountDTO.getUserBankAccount());
            userAccount.setUserAccountBalance(userAccountDTO.getUserAccountBalance());
            userAccount.setAccountPassword(userAccountDTO.getAccountPassword());

            userAccount.setUsers(user);
try {
    userAccountRepo.save(userAccount);
}catch(Exception e){
    log.error("Excep[tyion occurred ",e);
    return ResponseEntity.badRequest().body(Map.of("message","Duplicate Entry"));
}

            UsersAccountHistory history = new UsersAccountHistory();
            history.setCurrentAccountBalance(userAccountDTO.getUserAccountBalance());
            history.setAmountTransacted(userAccountDTO.getUserAccountBalance());
            history.setAmountTransactedTime(history.getCurrentIndianTime());
            history.setEmail(currentUserEmail);


            return ResponseEntity.ok(Map.of("message"," Account addition successful"));
        }
        else {

            return ResponseEntity.badRequest().body(Map.of("message","Please use the same email entered while registering."));
        }
    }

}
