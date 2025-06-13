package com.org.StockEX.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // Injecting 'from' email from application.properties
    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtp(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);  //  This is required to avoid "can't determine local email address"
        message.setTo(toEmail);
        message.setSubject("OTP Verification - StockEX");
        message.setText("Your OTP is: " + otp + "\nIt is valid for 3 minutes.");

        mailSender.send(message);
        System.out.println("OTP sent to " + toEmail);
    }
}
