package com.org.StockEX.controller;

import com.org.StockEX.DTO.*;
import com.org.StockEX.Entity.Havings;
import com.org.StockEX.Entity.Stocks;
import com.org.StockEX.Entity.TransactionsHistory;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.repository.HoldingsRepo;
import com.org.StockEX.repository.TransactionRepo;
import com.org.StockEX.repository.UserAccountHistoryRepo;
import com.org.StockEX.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private BuyStocksService buyStocksService;

    @PreAuthorize("hasRole('USER')")
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/buy")
    public ResponseEntity<?> buyStock(@Valid @RequestBody BuyStocksDTO buyStocksDTO){
            return buyStocksService.buyStocks(buyStocksDTO);
    }


    @Autowired
    private SellStocksService sellStocksService;
    @PostMapping("/sell")
    public ResponseEntity<?> sellStocks(@RequestBody SellStocksDTO sellStocksDTO){

        return sellStocksService.sellStocks(sellStocksDTO);
    }

    @Autowired
    private ShowStocksService showStocks;

    @Autowired
    private HoldingsRepo holdingsRepo;

    private String currentUserEmail ;

    @GetMapping("/showStocks")
    public List<Stocks> showStocks(){

        return showStocks.showStocks();
    }

    @GetMapping("/myStocks")
    public List<MyStockDisplayDTO> myStocks(){
        currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        List<MyStockDisplayDTO> mystocks = holdingsRepo.findFullStockDisplayByEmail(currentUserEmail);
        System.out.println("\n\n\n"+mystocks);
        return mystocks;
    }


    @Autowired
    private UpdateAccountBalanceService userAccountBalanceService;

    @PatchMapping("/deposit")
    public ResponseEntity<?> updateAccountBalance(@RequestBody UpdateAccountBalanceDTO updateAccountBalanceDTO){

        return userAccountBalanceService.updateAccountBalance(updateAccountBalanceDTO);

    }



    @Autowired
    private UserAccountService userAccountService;


    @PostMapping("/accountAddition")
    public ResponseEntity<?> accountAddition(@Valid @RequestBody UserAccountDTO userAccountDTO){


        return userAccountService.addAccount(userAccountDTO) ;
    }


    @Autowired
    WithdrawService withdrawService;

    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody UpdateAccountBalanceDTO updateAccountBalanceDTO){
        return withdrawService.withdraw(updateAccountBalanceDTO);
    }


    @Autowired
    private TransactionRepo transactionRepo;

    @GetMapping("/recentStockTransactions")

    public ResponseEntity<List<TransactionsHistory>> recentStockTransactions(){
        currentUserEmail= SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(transactionRepo.findRecentByEmail(currentUserEmail));


    }

    @GetMapping("/allStockTransactions")

    public ResponseEntity<List<TransactionsHistory>> allStockTransactions(){

        currentUserEmail= SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(transactionRepo.findByEmail(currentUserEmail));


    }

    @Autowired
    private ShowUserAccountHistoryService showUserAccountHistoryService;

    @GetMapping("/recentAccountTransactions")

    public ResponseEntity<List<UsersAccountHistory>> recentAccountTransactions(){
        currentUserEmail= SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(showUserAccountHistoryService.recentAccountTransactionsService(currentUserEmail));

    }

    @GetMapping("/allAccountTransactions")

    public ResponseEntity<List<UsersAccountHistory>> allAccountTransactions(){

        currentUserEmail= SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok(showUserAccountHistoryService.allAccountTransactionsService(currentUserEmail));

    }

    @PostMapping("/logout")
    public String logout() {

        SecurityContextHolder.clearContext();


        return "Logged out successfully";
    }


    @Autowired
    private ShowMyProfile showMyProfile;


    @GetMapping("/myProfile")
    public MyProfileDTO myProfile(){

        currentUserEmail= SecurityContextHolder.getContext().getAuthentication().getName();


        return showMyProfile.showMyProfile(currentUserEmail) ;
    }




}
