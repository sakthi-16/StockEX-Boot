package com.org.StockEX.repository;

import com.org.StockEX.Entity.ConfirmBuyStocks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmBuyStocksRepo extends JpaRepository<ConfirmBuyStocks,Long> {

    @Query(value = "SELECT * FROM confirm_buy_stocks WHERE seller_order_no = :actualSellerOrderNo And user_id= :userId", nativeQuery = true)
    Optional<ConfirmBuyStocks> findBySellerOrderNoAndUserId(String actualSellerOrderNo,Long userId);

    @Query(value = "SELECT * FROM confirm_buy_stocks WHERE user_mail = :email", nativeQuery = true)
    List<ConfirmBuyStocks> findByUserMail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE confirm_buy_stocks SET transaction_status ='Success'  WHERE seller_order_no = :actualSellerOrderNo", nativeQuery = true)
    void updateTransactionStatus( @Param("actualSellerOrderNo") String actualSellerOrderNo);




}
