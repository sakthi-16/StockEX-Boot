package com.org.StockEX.service;

import com.org.StockEX.Entity.Favourites;
import com.org.StockEX.repository.CreateCollectionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@Service
public class CreateCollectionService {

    @Autowired
    private CreateCollectionRepo createCollectionRepo;


    public ResponseEntity<?> createCollection(String collectionName,String userMail,Long userId){

        Optional<Favourites> duplicateCheck=createCollectionRepo.findByUserIdAndCollectionName(userId,collectionName.trim().toLowerCase(Locale.ROOT));

        if(!duplicateCheck.isEmpty()){
            return ResponseEntity.badRequest().body(Map.of("message",collectionName+" Collection already exist in your collections"));
        }

        Favourites favourites =new Favourites();

               favourites.setCollectionName(collectionName.toLowerCase(Locale.ROOT));
               favourites.setEmail(userMail);
               favourites.setUserId(userId);


               createCollectionRepo.save(favourites);

               return ResponseEntity.ok(Map.of("message","Collection "+collectionName+" Created"));
    }


}
