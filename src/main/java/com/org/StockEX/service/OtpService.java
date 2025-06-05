package com.org.StockEX.service;

import com.org.StockEX.Entity.OtpRequest;
import com.org.StockEX.repository.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final EmailService emailService;


    public String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public String sendOtpToUser(String email) {
        Optional<OtpRequest> existing = otpRepository.findByEmail(email);
        OtpRequest otpRequest = existing.orElse(new OtpRequest());

        if (existing.isPresent() && otpRequest.getAttemptCount() >= 3) {
            if (Duration.between(otpRequest.getLastAttemptTime(), LocalDateTime.now()).toMinutes() < 15) {
                return "OTP limit exceeded. Try again after 15 minutes.";
            } else {
                otpRequest.setAttemptCount(0);
            }
        }

        String otp = generateOtp();
        otpRequest.setEmail(email);
        otpRequest.setOtp(otp);
        otpRequest.setExpiryTime(LocalDateTime.now().plusMinutes(3));
        otpRequest.setAttemptCount(otpRequest.getAttemptCount() + 1);
        otpRequest.setLastAttemptTime(LocalDateTime.now());
        otpRequest.setVerified(false);

        otpRepository.save(otpRequest);

        emailService.sendOtp(email, otp);

        return "OTP sent to email.";
    }

    public String verifyOtp(String email, String otp) {
        OtpRequest request = otpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No OTP found"));

        if (request.getExpiryTime().isBefore(LocalDateTime.now())) {
            return "OTP expired.";
        }

        if (!request.getOtp().equals(otp)) {
            return "Invalid OTP.";
        }

        request.setVerified(true);
        otpRepository.save(request);
        return "OTP verified.";
    }
}
