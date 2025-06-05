package com.org.StockEX.repository;

import com.org.StockEX.DTO.MyStockDisplayDTO;
import com.org.StockEX.Entity.Havings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoldingsRepo extends JpaRepository<Havings,Long> {

Optional<Havings> findByStockNameAndEmail(String stockName, String email);
List<Havings> findByEmail(String email);
    @Query("""
    SELECT new com.org.StockEX.DTO.MyStockDisplayDTO(
        h.stockName,
        h.stockImage,
        h.holdingStocks,
        h.avgStockPrice,
        h.totalHoldingsValue,
        s.stockPrice,
        CASE 
            WHEN h.avgStockPrice < s.stockPrice THEN 'Gain'
            WHEN h.avgStockPrice > s.stockPrice THEN 'Loss'
            ELSE 'No Change'
        END
    )
    FROM Havings h
    JOIN Stocks s ON h.stockName = s.stockName
    WHERE h.email = :email
""")
    List<MyStockDisplayDTO> findFullStockDisplayByEmail(String email);

}
