package com.org.StockEX.repository;

import com.org.StockEX.DTO.UserAccountHistoryDTO;
import com.org.StockEX.Entity.TransactionsHistory;
import com.org.StockEX.Entity.UsersAccountHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserAccountHistoryRepo extends JpaRepository<UsersAccountHistory, Integer> {

    @Query("SELECT ua FROM UsersAccountHistory ua WHERE ua.email = :email")
    List<UsersAccountHistory> findByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM Users_Account_History  WHERE email = :email ORDER BY account_transaction DESC ", nativeQuery = true)
    List<UsersAccountHistory> findRecentByEmail(@Param("email") String email);
}
