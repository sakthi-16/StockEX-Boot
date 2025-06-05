package com.org.StockEX.service;

import com.org.StockEX.DTO.TransactionHistoryDTO;
import com.org.StockEX.DTO.UserAccountHistoryDTO;
import com.org.StockEX.Entity.TransactionsHistory;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.repository.TransactionRepo;
import com.org.StockEX.repository.UserAccountHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShowUserAccountHistoryService {

    @Autowired
    private UserAccountHistoryRepo userAccountHistoryRepo;

    public List<UsersAccountHistory> allAccountTransactionsService(String email){

        return userAccountHistoryRepo.findByEmail(email);


    }

    public List<UsersAccountHistory> recentAccountTransactionsService(String email) {
        return  userAccountHistoryRepo.findRecentByEmail(email);


    }

}
