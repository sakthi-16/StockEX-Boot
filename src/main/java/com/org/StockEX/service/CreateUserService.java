package com.org.StockEX.service;

import com.org.StockEX.DTO.UserCredentialsDTO;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.Usercredentialsrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class CreateUserService {



    @Autowired
    private Usercredentialsrepo usercredentialsrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> createUserService(UserCredentialsDTO userCredentialsDTO){



        Optional<UsersCredentials> existingUser=usercredentialsrepo.findByEmail(userCredentialsDTO.getEmail());

        System.out.println(existingUser);

        if(existingUser.isPresent()) {

         return ResponseEntity.badRequest().body(Map.of("message","Email is already registered!"));

        }

        UsersCredentials user=new UsersCredentials();
        user.setUsername(userCredentialsDTO.getUsername());
        user.setEmail(userCredentialsDTO.getEmail());
        user.setPan(userCredentialsDTO.getPan());
        user.setMobile(userCredentialsDTO.getMobile());
        user.setDob(userCredentialsDTO.getDob());
        user.setPassword(passwordEncoder.encode(userCredentialsDTO.getPassword()));
        user.setLastUsedPassword(passwordEncoder.encode(userCredentialsDTO.getPassword()));

        usercredentialsrepo.save(user);

        return ResponseEntity.ok(Map.of("message","successfully stored the details of the user, whose generated id is "+user.getUserId()));
    }
}
