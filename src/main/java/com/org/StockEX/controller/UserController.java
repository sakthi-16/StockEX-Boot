package com.org.StockEX.controller;

import com.org.StockEX.DTO.*;
import com.org.StockEX.Entity.ConfirmBuyStocks;
import com.org.StockEX.Entity.Stocks;
import com.org.StockEX.Entity.TransactionsHistory;
import com.org.StockEX.Entity.UsersAccountHistory;
import com.org.StockEX.repository.HoldingsRepo;
import com.org.StockEX.repository.TransactionRepo;
import com.org.StockEX.service.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private BuyStocksService buyStocksService;

    @Autowired
    private ShowTransactionStatus showTransactionStatus;

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




    @Autowired
    private ShowMyProfile showMyProfile;


    @GetMapping("/myProfile")
    public MyProfileDTO myProfile(){

        currentUserEmail= SecurityContextHolder.getContext().getAuthentication().getName();


        return showMyProfile.showMyProfile(currentUserEmail) ;
    }



    @PostMapping("/buy-confirm")
//    public ResponseEntity<?> confirmBuy(@RequestParam Map<String,String> fpxResponse, HttpServletResponse httpServletResponse) throws IOException {
    public void confirmBuy(@RequestParam Map<String,String> fpxResponse, HttpServletResponse response) throws IOException {

        fpxResponse.forEach((key,value)->System.out.println(key+"="+value));
        String sellerOrderNo = fpxResponse.get("fpx_sellerOrderNo");
        String debitAuthCode = fpxResponse.get("fpx_debitAuthCode");
        String txnAmount = fpxResponse.get("fpx_txnAmount");
        String txnCurrency = fpxResponse.get("fpx_txnCurrency");
        String time= fpxResponse.get("time");
        String date=fpxResponse.get("date");
        String transactionId=fpxResponse.get("fpx_fpxTxnId");
        System.out.println(sellerOrderNo+" "+debitAuthCode+" "+txnCurrency+" "+txnAmount+" ");
        boolean status=buyStocksService.confirmBuyStocks(sellerOrderNo,debitAuthCode,txnAmount,txnCurrency);
        System.out.println("\n\n  status: "+status+"\n\n\n");
        String redirectUrl = String.format("http://localhost:4200/payment-status?code=%s&id=%s&order=%s&amount=%s&currency=%s&date=%s&time=%s",
                debitAuthCode, transactionId, sellerOrderNo, txnAmount, txnCurrency, date, time);

        response.sendRedirect(redirectUrl);

//        return buyStocksService.confirmBuyStocks(sellerOrderNo,debitAuthCode,txnAmount,txnCurrency);
    }



    @GetMapping("/transaction-status")
    public ResponseEntity<List<ConfirmBuyStocks>> transactionStatus(){
        String currentUserMail=SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok( showTransactionStatus.transactionStatus(currentUserMail));



    }



}
