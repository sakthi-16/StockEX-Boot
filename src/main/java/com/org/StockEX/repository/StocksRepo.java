package com.org.StockEX.repository;


import com.org.StockEX.Entity.Stocks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface StocksRepo extends JpaRepository<Stocks,Integer> {

    Optional<Stocks> findByStockName(String stockName);

    @Query(value = "SELECT * FROM stocks  WHERE total_stocks <> 0",nativeQuery = true)
    List<Stocks> getAllStocks();

    @Query("SELECT s.stockPrice FROM Stocks s WHERE s.stockName = :stockName")
    BigDecimal getInstantStockPrice(@Param("stockName") String stockName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE stocks SET stock_price = :amount WHERE stock_name = :stockName", nativeQuery = true)
    int updateStockPrice(@Param("amount") BigDecimal amount, @Param("stockName") String stockName);


    @Modifying
    @Transactional
    @Query(value = "UPDATE stocks SET total_stocks = total_stocks + :sellQuantity WHERE stock_name = :stockName", nativeQuery = true)
    void updateStockQuantity(@Param("sellQuantity") Integer sellQuantity, @Param("stockName") String stockName);





}
