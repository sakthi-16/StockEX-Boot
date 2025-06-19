package com.org.StockEX.service;

import com.org.StockEX.DTO.StockAdditionToCollectionDTO;
import com.org.StockEX.Entity.CollectionStocks;
import com.org.StockEX.repository.AddStockToCollectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Optional;

@Service
public class AddStockToCollection {


    @Autowired
    private AddStockToCollectionRepo addStockToCollectionRepo;

    public ResponseEntity<?> addStockToCollection(@RequestBody StockAdditionToCollectionDTO stockAdditionToCollectionDTO,Long collectionId,String mail,Long userId){

        String collectionName=stockAdditionToCollectionDTO.getCollectionName();
        String stockName=stockAdditionToCollectionDTO.getStockName();

        Optional<CollectionStocks> duplicateCheck=addStockToCollectionRepo.findByCollectionNameAndStockNameAndCollectionIdAndUserMail(collectionName,stockName,collectionId,mail);
        if(!duplicateCheck.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("message",stockName+" Stock already exists in the same collection."));
        }

        CollectionStocks collectionStocks=new CollectionStocks();
        collectionStocks.setCollectionId(collectionId);
        collectionStocks.setCollectionName(collectionName);
        collectionStocks.setStockName(stockName);
        collectionStocks.setStockPriceWhenAdded(stockAdditionToCollectionDTO.getStockPriceWhenAdded());
        collectionStocks.setUserMail(mail);
        collectionStocks.setUserId(userId);

        addStockToCollectionRepo.save(collectionStocks);


        return ResponseEntity.ok(Map.of("message",stockName+" Stock added to "+collectionName+" successfully."));
    }

}
