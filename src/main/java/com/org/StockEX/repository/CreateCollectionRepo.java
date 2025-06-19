package com.org.StockEX.repository;

import com.org.StockEX.Entity.Favourites;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CreateCollectionRepo extends JpaRepository<Favourites,Long> {

    Optional<Favourites> findByUserIdAndCollectionName(Long userId, String collectionName);

    @Query(value = "Select collection_name from favourites where email= :mail and flag = true",nativeQuery = true)
    List<String> getAllCollectionsOfUser(@Param("mail") String mail);

    @Query(value="Select collection_id from favourites where email= :currentUserMail and collection_name= :collectionName ",nativeQuery = true)
    Long getCollectionId(@Param("currentUserMail")String currentUserMail, @Param("collectionName")String collectionName);

    @Modifying
    @Transactional
    @Query(value="update favourites set flag = 0 where user_id= :userId and collection_id= :collectionId",nativeQuery = true)
    int changeTheFlagToZero(@Param("userId")Long userId,@Param("collectionId")Long collectionId);

    @Modifying
    @Transactional
    @Query(value="update favourites set flag = 1 where user_id= :userId and collection_id= :collectionId",nativeQuery = true)
    int changeTheFlagToOne(@Param("userId")Long userId,@Param("collectionId")Long collectionId);

    @Modifying
    @Transactional
    @Query(value = "delete from favourites where user_id= :userId and collection_id= :collectionId",nativeQuery = true)
    int deleteCollection(@Param("userId")Long userId,@Param("collectionId")Long collectionId);

}
