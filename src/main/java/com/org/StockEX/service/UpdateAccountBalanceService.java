package com.org.StockEX.service;

import com.org.StockEX.DTO.UpdateAccountBalanceDTO;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.UserAccountHistoryRepo;
import com.org.StockEX.repository.UserAccountRepo;
import com.org.StockEX.repository.Usercredentialsrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;


@Service
public class UpdateAccountBalanceService {

    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private Usercredentialsrepo userCredentialsRepo;

    @Autowired
    private UserAccountHistoryRepo userAccountHistoryRepo;






    public ResponseEntity<?> updateAccountBalance(@RequestBody UpdateAccountBalanceDTO updateAccountBalanceDTO) {
        int updatedRows = 0;

            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<UsersCredentials> chosenUser = userCredentialsRepo.findByEmail(currentUserEmail);

            if (chosenUser.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message","Buyer Not Found"));
            }

            UsersCredentials customer = chosenUser.get();
            Long userId = customer.getUserId();
        if (! updateAccountBalanceDTO.getAccountPIN().equals(userAccountRepo.findAccountPasswordByUserId(userId))) {

             return ResponseEntity.badRequest().body(Map.of("message","Warning,Wrong PIN entered."));
        }
            BigDecimal amount = updateAccountBalanceDTO.getAmount();
            updatedRows = userAccountRepo.updateAccountBalance(amount, userId);
            BigDecimal currentBalance=userAccountRepo.getAccountBalance(userId);

        UsersAccountHistory history = new UsersAccountHistory();
        history.setCurrentAccountBalance(currentBalance);
        history.setAmountTransacted(amount);
        history.setAmountTransactedTime(history.getCurrentIndianTime());
        history.setTransactionType("deposit");
        history.setEmail(currentUserEmail);

        userAccountHistoryRepo.save(history);


            return updatedRows > 0 ? ResponseEntity.ok(Map.of("message","₹"+amount+" credited to your account.Your Current Balance is "+userAccountRepo.getAccountBalance(userId)+".")) : ResponseEntity.badRequest().body(Map.of("message","Something Went Wrong."));
        }
    }

