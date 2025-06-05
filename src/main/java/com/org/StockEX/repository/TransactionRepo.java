package com.org.StockEX.repository;

import com.org.StockEX.DTO.TransactionHistoryDTO;
import com.org.StockEX.Entity.TransactionsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionsHistory,Integer> {

    @Query("SELECT th FROM TransactionsHistory th WHERE th.email = :email")
    List<TransactionsHistory> findByEmail(@Param("email") String email);


    @Query(value = "SELECT * FROM transactions_history  WHERE email = :email ORDER BY transaction DESC ", nativeQuery = true)
    List<TransactionsHistory> findRecentByEmail(@Param("email") String email);



}
