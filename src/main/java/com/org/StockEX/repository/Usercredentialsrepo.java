package com.org.StockEX.repository;

import com.org.StockEX.DTO.MyProfileDTO;
import com.org.StockEX.Entity.UsersCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Usercredentialsrepo extends JpaRepository<UsersCredentials, Long> {
  Optional<UsersCredentials> findByEmail(String email);


  @Query(value = "Select user_id from users_credentials where email= :currentUserMail ",nativeQuery = true)
  Long getUserIdByMail(@Param("currentUserMail") String currentUserMail);

//  @Query("SELECT uc FROM UsersCredentials uc WHERE uc.userId = :userId")
//  Optional<UsersCredentials> findById( Long userId);







}
