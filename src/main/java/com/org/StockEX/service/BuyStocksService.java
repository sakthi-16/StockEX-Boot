package com.org.StockEX.service;

import com.org.StockEX.DTO.BuyStocksDTO;
import com.org.StockEX.DTO.ConfirmBuyStocksDTO;
import com.org.StockEX.DTO.FPXResponseDTO;
import com.org.StockEX.Entity.*;
import com.org.StockEX.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
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

    @Autowired
    private ConfirmBuyStocksRepo confirmBuyStocksRepo;



    private static final String tid = "27965678";
    private static final String mid = "FPX000000054555";
    private static final String subMID = "201100000012450";


    private static final int ITERATION_COUNT = 65536; // Example value
    private static final int CIPHER_KEY_LEN = 256;    // Example value in bits




    @Transactional
    public ResponseEntity<?> buyStocks(BuyStocksDTO buyStocksDTO) {

        String sellerOrderNo;



        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UsersCredentials> chosenUser = userCredentialsRepo.findByEmail(currentUserEmail);

        if (chosenUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","Buyer Not Found"));
        }

        UsersCredentials customer = chosenUser.get();
        Long userId = customer.getUserId();
        System.out.println("\n\n"+userId+"\n\nuserId");

        sellerOrderNo=userId+"B"+generateSellerOrderNo();

        Optional<UsersAccount> buyersAccount = userAccountRepo.findAccountByUserId(userId);
        if (buyersAccount.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message","Bank account and PIN has not been set."));
        }

        String accountPIN = userAccountRepo.findAccountPasswordByUserId(userId);
        if (!buyStocksDTO.getAccountPIN().equals(accountPIN)) {
            return ResponseEntity.badRequest().body(Map.of("message","Warning, Wrong PIN entered!"));
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
        BigDecimal total = subtotal.add(brokerage).add(exchangeCharges).add(gst).setScale(2,RoundingMode.HALF_UP);

        System.out.println("total: " + total + " subtotal: " + subtotal);
        BigDecimal nowBalance = userAccountRepo.getAccountBalance(userId);
        System.out.println("CURRENTBALANCE: " + nowBalance);

        if (nowBalance.compareTo(total) < 0 ) {

            return ResponseEntity.badRequest().body(Map.of("message","Insufficient Balance!"));
        }

        double amount=total.doubleValue();
        String checkSum;
        String minifieldString= amount+"|"+sellerOrderNo+"|"+subMID;

        checkSum=encryptPayload(minifieldString,mid,tid);
        System.out.println(checkSum);

        String bankName=buyStocksDTO.getBankName();
        String bankCode=buyStocksDTO.getBankCode();

        ConfirmBuyStocksDTO confirmDTO = new ConfirmBuyStocksDTO();
        confirmDTO.setMessage("Validation successful. Please confirm payment.");
        confirmDTO.setAmountToPay(total);
        confirmDTO.setSubTotal(subtotal);
        confirmDTO.setBrokerage(brokerage);
        confirmDTO.setUserMail(currentUserEmail);
        confirmDTO.setExchangeCharges(exchangeCharges);
        confirmDTO.setGst(gst);
        confirmDTO.setSellerOrderNo(sellerOrderNo);
        confirmDTO.setCheckSum(checkSum);
        confirmDTO.setStockName(stock.getStockName());
        confirmDTO.setStockQuantity(requestedQuantity);
        confirmDTO.setUserId(userId);
        confirmDTO.setUserMail(currentUserEmail);
        confirmDTO.setBankName(bankName);
        confirmDTO.setBankCode(bankCode);
        confirmDTO.setSubMID(subMID);
        confirmDTO.setTransactionStatus("Failure");



        ConfirmBuyStocks confirmBuyStocks=new ConfirmBuyStocks();
        confirmBuyStocks.setMessage("Validation successful. Please confirm payment.");
        confirmBuyStocks.setAmountToPay(total);
        confirmBuyStocks.setSubTotal(subtotal);
        confirmBuyStocks.setBrokerage(brokerage);
        confirmBuyStocks.setExchangeCharges(exchangeCharges);
        confirmBuyStocks.setGst(gst);
        confirmBuyStocks.setSubMID(subMID);
        confirmBuyStocks.setUserId(userId);
        confirmBuyStocks.setUserMail(currentUserEmail);
        confirmBuyStocks.setSellerOrderNo(sellerOrderNo);
        confirmBuyStocks.setStockName(stock.getStockName());
        confirmBuyStocks.setStockQuantity(requestedQuantity);
        confirmBuyStocks.setBankCode(bankCode);
        confirmBuyStocks.setBankName(bankName);
        confirmBuyStocks.setCheckSum(checkSum);
        confirmBuyStocks.setTransactionStatus("Failure");

        System.out.println(bankName+"hello"+bankCode);
        System.out.println("SELOLOERORDERNUMBER"+sellerOrderNo+"\n\n\n");


        confirmBuyStocksRepo.save(confirmBuyStocks);

// confirmDTO.setSelectedBank(...); // if frontend sends it

        return ResponseEntity.ok(confirmDTO);

}

@Transactional
public boolean confirmBuyStocks(String sellerOrderNo, String debitAuthCode, String txnAmount, String txnCurrency){

        String sellerOrderNoBeforeSplit=sellerOrderNo;
         int indexOfB = sellerOrderNoBeforeSplit.indexOf("B");
         String user_id= sellerOrderNoBeforeSplit.substring(0,indexOfB);
         int partBeforeUnderscore=sellerOrderNoBeforeSplit.indexOf("_");
         String sellerOrderNumber=sellerOrderNoBeforeSplit.substring(0,partBeforeUnderscore);


    String  actualSellerOrderNo = sellerOrderNumber;
    String  fpxDebitAuthCode = debitAuthCode;
    Long userId=Long.parseLong(user_id);

    Optional<ConfirmBuyStocks> stockDataRegistered= confirmBuyStocksRepo.findBySellerOrderNoAndUserId(actualSellerOrderNo,userId);

    if(stockDataRegistered.isEmpty()){
        System.out.println("\ndue no data");
        ConfirmBuyStocks cnfrmstocks=stockDataRegistered.get();
        System.out.println(cnfrmstocks.getStockName());
        return false;
//        return ResponseEntity.badRequest().body(Map.of("message","Details not found in the database regarding the stock you're requesting to buy."));
    }




    if(!fpxDebitAuthCode.equals("00")){

        System.out.println("\ndue to debitauthcode");

        return false;

    }
    ConfirmBuyStocks confirmStocksForDeductions=stockDataRegistered.get();

    String stockName=confirmStocksForDeductions.getStockName();
    int stockQuantity=confirmStocksForDeductions.getStockQuantity();
    String currentUserEmail=confirmStocksForDeductions.getUserMail();

    Optional<UsersCredentials> chosenUser=userCredentialsRepo.findById(userId);

    if (chosenUser.isEmpty()) {
        System.out.println("\ndue to user not found");
        return false;
//        return ResponseEntity.badRequest().body(Map.of("message","Buyer Not Found"));
    }

    UsersCredentials customer = chosenUser.get();

    String accountPIN = userAccountRepo.findAccountPasswordByUserId(userId);


    Optional<UsersAccount> buyersAccount = userAccountRepo.findAccountByUserId(userId);
    if (buyersAccount.isEmpty()) {
        System.out.println("\ndue to user account not found");
        return false;
//        return ResponseEntity.badRequest().body(Map.of("message","No such user is found."));
    }

    UsersAccount buyerAccount = buyersAccount.get();

    Optional<Stocks> stocks = stocksRepo.findByStockName(stockName);
    if (stocks.isEmpty()) {
        System.out.println("\ndue to stock not found");
        return false;
//        return ResponseEntity.badRequest().body(Map.of("message","Stock not found."));
    }

    Stocks stock = stocks.get();
    int availableStock = stock.getTotalStocks();
    int requestedQuantity = stockQuantity;
    if ( requestedQuantity<0 ) {
        System.out.println("\ndue to negative value entered as quantity");
        return false;
//        return ResponseEntity.badRequest().body(Map.of("message","Entered value is negative.Warning not allowed option!"));
    }

    if (availableStock < requestedQuantity) {

        System.out.println("\ndue to less quantity available");
        return false;
//        return ResponseEntity.badRequest().body(Map.of("message","Entered quantity is larger than the available."));
    }

    BigDecimal askedQuantity = BigDecimal.valueOf(requestedQuantity);
    BigDecimal currentStockPrice = stocksRepo.getInstantStockPrice(stockName);
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

        System.out.println("\ndue to insufficient balance");
        return false;

//        return ResponseEntity.badRequest().body(Map.of("message","Insufficient Balance!"));
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
        history.setTransactionType("withdraw");
        history.setEmail(currentUserEmail);


        userAccountRepo.save(buyerAccount);
        stocksRepo.save(stock);
        transactionRepo.save(transaction);
        userAccountHistoryRepo.save(history);

        confirmBuyStocksRepo.updateTransactionStatus(actualSellerOrderNo);


        return true;

//        return ResponseEntity.ok(Map.of("message","Payment Successful."+"Stocks sold for "+total+"  added to your Holdings."));
    }



        public String generateSellerOrderNo() {

            String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
            return  timestamp ;
        }

    public static String encryptPayload(String minifiedString, String param1, String param2) {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] iv = new byte[16];
            secureRandom.nextBytes(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(param1.toCharArray(), param2.getBytes(), ITERATION_COUNT, CIPHER_KEY_LEN);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

            byte[] cipherText = cipher.doFinal(minifiedString.getBytes(StandardCharsets.UTF_8));
            byte[] encryptedData = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, encryptedData, 0, iv.length);
            System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}





