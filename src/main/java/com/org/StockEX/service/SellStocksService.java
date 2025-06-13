package com.org.StockEX.service;




import com.org.StockEX.DTO.SellStocksDTO;
import com.org.StockEX.Entity.Havings;
import com.org.StockEX.Entity.TransactionsHistory;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

@Service
public class SellStocksService {

    @Autowired
    private HoldingsRepo holdingsRepo;

    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private UserAccountHistoryRepo userAccountHistoryRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private StocksRepo stocksRepo;

    @Autowired
    Usercredentialsrepo userCredentialRepo;


    @Transactional
    public ResponseEntity<?> sellStocks(SellStocksDTO sellStocksDTO) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UsersCredentials> chosenUser = userCredentialRepo.findByEmail(currentUserEmail);

        if (chosenUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","Buyer Not Found"));
        }

        UsersCredentials customer = chosenUser.get();
        Long userId = customer.getUserId();
        String stockName = sellStocksDTO.getStockName();
        Integer sellQuantity = sellStocksDTO.getStockQuantity();

        Optional<Havings> having = holdingsRepo.findByStockNameAndEmail(stockName,currentUserEmail);

        if (having.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","You don't have this stock."));
        }

        Havings holding = having.get();

        String accountPIN = userAccountRepo.findAccountPasswordByUserId(userId);
        if (!sellStocksDTO.getAccountPIN().equals(accountPIN)) {
            return ResponseEntity.badRequest().body(Map.of("message","Warning, Wrong PIN entered!"));
        }

        if (holding.getHoldingStocks() < sellQuantity) {
            return ResponseEntity.badRequest().body(Map.of("message","Not enough stocks to sell."));
        }
        if ( sellQuantity<0 ) {
            return ResponseEntity.badRequest().body(Map.of("message","Entered value is negative.Warning not allowed option!"));
        }

        BigDecimal currentStockPrice = stocksRepo.getInstantStockPrice(stockName);
        BigDecimal quantityToSell = BigDecimal.valueOf(sellQuantity);
        BigDecimal subtotal = quantityToSell.multiply(currentStockPrice);

        BigDecimal brokerage = subtotal.multiply(BigDecimal.valueOf(0.005));
        if (brokerage.compareTo(BigDecimal.valueOf(20)) > 0) {
            brokerage = BigDecimal.valueOf(20);
        }

        BigDecimal exchangeCharges = subtotal.multiply(BigDecimal.valueOf(0.0000345));
        BigDecimal gst = (brokerage.add(exchangeCharges)).multiply(BigDecimal.valueOf(0.18));

        BigDecimal totalReceivable = subtotal.subtract(brokerage).subtract(exchangeCharges).subtract(gst);

        holding.setHoldingStocks(holding.getHoldingStocks() - sellQuantity);

        BigDecimal newHoldingsValue = BigDecimal.valueOf(holding.getHoldingStocks()).multiply(holding.getAvgStockPrice());
        holding.setTotalHoldingsValue(newHoldingsValue);

        if (holding.getHoldingStocks() == 0) {
            holdingsRepo.delete(holding);
        } else {
            holdingsRepo.save(holding);
        }

        userAccountRepo.increaseBalance(totalReceivable,userId);

        UsersAccountHistory accountHistory = new UsersAccountHistory();
        accountHistory.setEmail(currentUserEmail);
        accountHistory.setAmountTransacted(totalReceivable);
        accountHistory.setAmountTransactedTime(accountHistory.getCurrentIndianTime());
        accountHistory.setCurrentAccountBalance(userAccountRepo.getAccountBalance(userId));
        accountHistory.setTransactionType("deposit");
        userAccountHistoryRepo.save(accountHistory);

        TransactionsHistory transactionsHistory = new TransactionsHistory();
        transactionsHistory.setEmail(currentUserEmail);
        transactionsHistory.setStockName(stockName);
        transactionsHistory.setStockImage(holding.getStockImage());
        transactionsHistory.setAmountCameInOrGoneOut(totalReceivable);
        transactionsHistory.setTransactionType("deposit");
        transactionRepo.save(transactionsHistory);

        stocksRepo.updateStockQuantity(sellQuantity,stockName);



        return ResponseEntity.ok(Map.of("message","Stock sold successfully."));
    }
}

