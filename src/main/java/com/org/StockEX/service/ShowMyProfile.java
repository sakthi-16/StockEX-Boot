package com.org.StockEX.service;


import com.org.StockEX.DTO.MyProfileDTO;
import com.org.StockEX.Entity.UsersAccount;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.UserAccountRepo;
import com.org.StockEX.repository.Usercredentialsrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ShowMyProfile {
    @Autowired
    private Usercredentialsrepo userCredentialsRepo;

    @Autowired
    private UserAccountRepo userAccountRepo;

    private UsersAccount user;

    public MyProfileDTO showMyProfile(String email){

        Optional<UsersCredentials> chosenOne=userCredentialsRepo.findByEmail(email);
        if(chosenOne.isPresent() && !chosenOne.isEmpty()) {
             UsersCredentials chosen= chosenOne.get();
            Optional<UsersAccount> users =userAccountRepo.findAccountByUserId(chosen.getUserId());
            if(users.isPresent() && !users.isEmpty()){
                 user=users.get();
            }

            MyProfileDTO myProfile = new MyProfileDTO(chosen.getUsername(),chosen.getUserId(),chosen.getEmail(),user.getUserBankAccount(),user.getUserAccountBalance(),user.getAccountPassword());


            return myProfile;
        }
        return new MyProfileDTO();
    }
}

