package com.company.hrms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtp(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("HRMS Login OTP Verification");
        message.setText(
                "Dear User,\n\n" +
                "Your OTP for HRMS login is: " + otp +
                "\n\nThis OTP is valid for 5 minutes.\n" +
                "Do not share this OTP with anyone.\n\n" +
                "Regards,\nHRMS Team"
        );

        mailSender.send(message);
    }
}