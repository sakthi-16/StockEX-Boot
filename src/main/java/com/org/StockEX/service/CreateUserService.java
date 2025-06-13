package com.org.StockEX.service;

import com.org.StockEX.DTO.UserCredentialsDTO;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.repository.Usercredentialsrepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CreateUserService {



    @Autowired
    private Usercredentialsrepo usercredentialsrepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> createUserService(UserCredentialsDTO userCredentialsDTO){



        Optional<UsersCredentials> existingUser=usercredentialsrepo.findByEmail(userCredentialsDTO.getEmail());


        if(existingUser.isPresent()) {
            UsersCredentials register=existingUser.get();

            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Email already registered"));

        }




        UsersCredentials user=new UsersCredentials();
        user.setUsername(userCredentialsDTO.getUsername());
        user.setEmail(userCredentialsDTO.getEmail());
        user.setPan(userCredentialsDTO.getPan());
        user.setMobile(userCredentialsDTO.getMobile());
        user.setDob(userCredentialsDTO.getDob());
        user.setPassword(passwordEncoder.encode(userCredentialsDTO.getPassword()));
        user.setLastUsedPassword(passwordEncoder.encode(userCredentialsDTO.getPassword()));

try{
        usercredentialsrepo.save(user);
} catch(Exception e){
    log.error("Exception occurred while inserting {}",e.getMessage(),e) ;
    return ResponseEntity.badRequest().body(Map.of("message","PAN id is already registered."));
}

        return ResponseEntity.ok(Map.of("message","successfully stored the details of the user, whose generated id is "+user.getUserId()));
    }
}
