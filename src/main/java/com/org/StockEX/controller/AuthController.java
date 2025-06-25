package com.org.StockEX.controller;

import com.org.StockEX.DTO.*;
import com.org.StockEX.Entity.UsersAccount;
import com.org.StockEX.Entity.UsersCredentials;
import com.org.StockEX.Security.JwtUtil;
import com.org.StockEX.repository.OtpRepository;
import com.org.StockEX.repository.UserAccountRepo;
import com.org.StockEX.repository.Usercredentialsrepo;
import com.org.StockEX.service.CreateUserService;
import com.org.StockEX.service.OtpService;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.webauthn.management.UserCredentialRepository;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserAccountRepo userAccountRepo;

    @Autowired
    private Usercredentialsrepo usercredentialsrepo;




    @CrossOrigin(origins = "http://localhost:4200", exposedHeaders = "Authorization")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response) {

        String email = loginRequestDTO.getEmail();
        String password = loginRequestDTO.getPassword();

        Optional<UsersCredentials> userOptional = userCredentialsRepo.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Email not registered"));
        }

        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            Map<String, Object> claims = Map.of("role", role);
            String token = jwtUtil.generateToken(claims, userDetails.getUsername());

            Long userId = userCredentialsRepo.getUserIdByMail(userDetails.getUsername());
            boolean isLinkedAccount = userAccountRepo.findAccountByUserId(userId).isPresent();

            response.addHeader("Authorization", "Bearer " + token);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "isLinkedAccount", isLinkedAccount
            ));

        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Incorrect password"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An error occurred during login"));
        }
    }




    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;



    @PostMapping("/signUp")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCredentialsDTO userCredentialsDTO){

        return createUserService.createUserService(userCredentialsDTO);

    }

    private final OtpService otpService;
    private final Usercredentialsrepo userCredentialsRepo;
    private final OtpRepository otpRepository;


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        String email = forgotPasswordDTO.getEmail();
        String result = otpService.sendOtpToUser(email);

        boolean success = result.equals("OTP sent to email.");

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", result);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody OtpVerifyDTO request) {
        String result = otpService.verifyOtp(request.getEmail(), request.getOtp());
        boolean success = result.equals("OTP verified.");

        Map<String, Object> response = new HashMap<>();
        response.put("success", success);
        response.put("message", result);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            System.out.println("\n\n"+request.getNewPassword()+" = "+request.getConfirmPassword()+"\n\n");

            return ResponseEntity.badRequest().body(Map.of("message","Passwords do not match"));
        }

        UsersCredentials user = userCredentialsRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(Map.of("message","New password cannot be same as old one"));
        }

        user.setLastUsedPassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userCredentialsRepo.save(user);

        return ResponseEntity.ok(Map.of("message","Password reset successfully"));
    }








}
