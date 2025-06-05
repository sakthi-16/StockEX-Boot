package com.org.StockEX.service;

import com.org.StockEX.DTO.BuyStocksDTO;
import com.org.StockEX.Entity.*;
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
public class BuyStocksService {

    @Autowired
    private Usercredentialsrepo userCredentialsRepo;

    @Autowired
    private StocksRepo stocksRepo;

    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private HoldingsRepo holdingsRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private UserAccountHistoryRepo userAccountHistoryRepo;

    @Transactional
    public ResponseEntity<?> buyStocks(BuyStocksDTO buyStocksDTO) {


        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UsersCredentials> chosenUser = userCredentialsRepo.findByEmail(currentUserEmail);

        if (chosenUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","Buyer Not Found"));
        }

        UsersCredentials customer = chosenUser.get();
        Long userId = customer.getUserId();

        String accountPIN = userAccountRepo.findAccountPasswordByUserId(userId);
        if (!buyStocksDTO.getAccountPIN().equals(accountPIN)) {
            return ResponseEntity.badRequest().body(Map.of("message","Warning, Wrong PIN entered!"));
        }

        Optional<UsersAccount> buyersAccount = userAccountRepo.findAccountByUserId(userId);
        if (buyersAccount.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","No such user is found."));
        }

        UsersAccount buyerAccount = buyersAccount.get();

        Optional<Stocks> stocks = stocksRepo.findByStockName(buyStocksDTO.getStockName());
        if (stocks.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","Stock not found."));
        }

        Stocks stock = stocks.get();
        int availableStock = stock.getTotalStocks();
        int requestedQuantity = buyStocksDTO.getStockQuantity();
        if ( requestedQuantity<0 ) {
            return ResponseEntity.badRequest().body(Map.of("message","Entered value is negative.Warning not allowed option!"));
        }

        if (availableStock < requestedQuantity) {
            return ResponseEntity.badRequest().body(Map.of("message","Entered quantity is larger than the available."));
        }

        BigDecimal askedQuantity = BigDecimal.valueOf(requestedQuantity);
        BigDecimal currentStockPrice = stocksRepo.getInstantStockPrice(buyStocksDTO.getStockName());
        BigDecimal subtotal = askedQuantity.multiply(currentStockPrice);

        BigDecimal brokerage = subtotal.multiply(BigDecimal.valueOf(0.005));
        if (brokerage.compareTo(BigDecimal.valueOf(20)) > 0) {
            brokerage = BigDecimal.valueOf(20);
        }

        BigDecimal exchangeCharges = subtotal.multiply(BigDecimal.valueOf(0.0000345));
        BigDecimal gst = (brokerage.add(exchangeCharges)).multiply(BigDecimal.valueOf(0.18));
        BigDecimal total = subtotal.add(brokerage).add(exchangeCharges).add(gst);

        System.out.println("total: " + total + " subtotal: " + subtotal);
        BigDecimal nowBalance = userAccountRepo.getAccountBalance(userId);
        System.out.println("CURRENTBALANCE: " + nowBalance);

        if (nowBalance.compareTo(total) < 0 ) {

            return ResponseEntity.badRequest().body(Map.of("message","Insufficient Balance!"));
        }

        BigDecimal nowChangedBalance=nowBalance.subtract(total);
        buyerAccount.setUserAccountBalance(nowChangedBalance);
        BigDecimal currentBalance = nowChangedBalance;
        stock.setTotalStocks(stock.getTotalStocks() - requestedQuantity);


        Optional<Havings> holdingsTaken = holdingsRepo.findByStockNameAndEmail(stock.getStockName(), currentUserEmail);
        Havings holdings = holdingsTaken.orElse(new Havings());

        if (holdingsTaken.isPresent()) {
            int existingQty = holdings.getHoldingStocks();

            BigDecimal newValue = currentStockPrice.multiply(BigDecimal.valueOf(requestedQuantity));

            int newQty = existingQty + requestedQuantity;
            BigDecimal avgPrice = holdings.getTotalHoldingsValue().add(newValue).divide(BigDecimal.valueOf(newQty), 2, RoundingMode.HALF_UP);

            holdings.setHoldingStocks(newQty);
            holdings.setAvgStockPrice(avgPrice);
            holdings.setOldStockPrice(currentStockPrice);
            holdings.setTotalHoldingsValue(holdings.getTotalHoldingsValue().add(newValue));
        } else {
            holdings.setEmail(currentUserEmail);
            holdings.setStockName(stock.getStockName());
            holdings.setStockImage(stock.getStocksImage());
            holdings.setHoldingStocks(requestedQuantity);
            holdings.setOldStockPrice(currentStockPrice);
            holdings.setAvgStockPrice(currentStockPrice);
            holdings.setTotalHoldingsValue(currentStockPrice.multiply(BigDecimal.valueOf(requestedQuantity)));
        }

        holdingsRepo.save(holdings);

        TransactionsHistory transaction = new TransactionsHistory();
        System.out.println("/n/n/n/n"+stock.getStockName()+stock.getStocksImage()+total+currentUserEmail);
        transaction.setStockName(stock.getStockName());
        transaction.setStockImage(stock.getStocksImage());
        transaction.setAmountCameInOrGoneOut(total);
        transaction.setTransactionType("withdraw");
        transaction.setEmail(currentUserEmail);


        UsersAccountHistory history = new UsersAccountHistory();
        System.out.println(currentBalance+" "+total+" "+history.getCurrentIndianTime()+" "+currentUserEmail);
        history.setCurrentAccountBalance(currentBalance);
        history.setAmountTransacted(total);
        history.setAmountTransactedTime(history.getCurrentIndianTime());
        history.setEmail(currentUserEmail);


        userAccountRepo.save(buyerAccount);
        stocksRepo.save(stock);
        holdingsRepo.save(holdings);
        transactionRepo.save(transaction);
        userAccountHistoryRepo.save(history);

        return ResponseEntity.ok(Map.of("message","Payment Successful."+"Stocks sold for "+total+"  added to your Holdings."));
    }
}
