package com.org.StockEX.repository;

import com.org.StockEX.DTO.FavouriteDisplayDTO;
import com.org.StockEX.Entity.CollectionStocks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AddStockToCollectionRepo extends JpaRepository<CollectionStocks,Long> {

    Optional<CollectionStocks> findByCollectionNameAndStockNameAndCollectionIdAndUserMail(String collectionName,String stockName,Long collectionId,String mail);

    @Query( value = """
            SELECT
                c.collection_name,
                s.stocks_image,
                c.stock_name,
                s.total_stocks,
                c.stock_price_when_added,
                s.stock_price,
               CAST( ROUND(((s.stock_price - c.stock_price_when_added) / c.stock_price_when_added) * 100, 2) AS DOUBLE) AS growth_rate
            FROM
                collection_stocks c
            JOIN
                stocks s ON c.stock_name = s.stock_name
            WHERE
                c.user_id = :userId
                AND c.collection_id = :collectionId and c.flag=true and s.total_stocks<>0;
            
            """,nativeQuery = true)
    List<FavouriteDisplayDTO> getFavouriteTable(@Param("userId")Long userId,@Param("collectionId")Long collectionId);



    @Modifying
    @Transactional
    @Query(value="update collection_stocks set flag = 0 where user_id= :userId and collection_id= :collectionId and stock_name= :stockName",nativeQuery = true)
    int changeTheFlagToZero(@Param("userId")Long userId,@Param("collectionId")Long collectionId,@Param("stockName")String stockName);


    @Modifying
    @Transactional
    @Query(value="update collection_stocks set flag = 1 where user_id= :userId and collection_id= :collectionId and stock_name= :stockName",nativeQuery = true)
    int changeTheFlagToOne(@Param("userId")Long userId,@Param("collectionId")Long collectionId,@Param("stockName")String stockName);

    @Modifying
    @Transactional
    @Query(value = "delete from collection_stocks where user_id = :userId and collection_id= :collectionId and stock_name= :stockName",nativeQuery = true)
    int deleteStockFromCollection(@Param("userId")Long userId,@Param("collectionId")Long collectionId,@Param("stockName")String stockName);

}
