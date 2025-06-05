package com.org.StockEX.repository;

import com.org.StockEX.Entity.UsersAccount;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserAccountRepo extends JpaRepository<UsersAccount,String> {

    @Query("SELECT ua FROM UsersAccount ua WHERE ua.users.userId = :userId")
    Optional<UsersAccount> findAccountByUserId(@Param("userId") Long userId);


    @Query("SELECT ua.accountPassword FROM UsersAccount ua WHERE ua.users.userId = :userId")
    String findAccountPasswordByUserId(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users_account SET user_account_balance = user_account_balance + :amount WHERE user_id = :userId", nativeQuery = true)
    int updateAccountBalance(@Param("amount") BigDecimal amount, @Param("userId") Long userId);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "UPDATE users_account SET user_account_balance = user_account_balance - :amount WHERE user_id = :userId", nativeQuery = true)
    int withdrawAmount(@Param("amount") BigDecimal amount, @Param("userId") Long userId);

    @Query("SELECT ua.userAccountBalance FROM UsersAccount ua WHERE ua.users.userId = :userId")
    BigDecimal getAccountBalance(@Param("userId") Long userId);

    @Modifying
    @org.springframework.transaction.annotation.Transactional
    @Query(value = "UPDATE users_account SET user_account_balance = user_account_balance + :amount WHERE user_id = :userId", nativeQuery = true)
    int increaseBalance(@Param("amount") BigDecimal amount, @Param("userId") Long userId);



}
