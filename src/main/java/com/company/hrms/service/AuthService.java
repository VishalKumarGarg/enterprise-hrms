package com.company.hrms.service;

import com.company.hrms.dto.EmployeeDto;
import com.company.hrms.dto.LoginResponseDto;
import com.company.hrms.dto.OtpRequestDto;
import com.company.hrms.entity.Employee;
import com.company.hrms.entity.OtpVerification;
import com.company.hrms.repository.EmployeeRepository;
import com.company.hrms.repository.OtpVerificationRepository;
import com.company.hrms.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private OtpVerificationRepository otpRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ModelService modelService;

    public void generateAndSendOtp(String email) {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // This is going to check if otp is not Verified and not Expired.
        otpRepository.findTopByEmployeeOrderByCreatedAtDesc(employee)
                .ifPresent(lastOtp -> {

                    boolean otpStillValid =
                            !lastOtp.isVerified() &&
                                    lastOtp.getExpiryTime().isAfter(LocalDateTime.now());

                    if (otpStillValid) {
                        throw new RuntimeException(
                                "OTP already sent. Please use the existing OTP."
                        );
                    }
                });
//        This will genrate the new Otp
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        String hashedOtp = passwordEncoder.encode(otp);

        OtpVerification otpEntity = OtpVerification.builder()
                .employee(employee)
                .otpHash(hashedOtp)
                .expiryTime(LocalDateTime.now().plusMinutes(5))
                .attemptCount(0)
                .verified(false)
                .build();

        otpRepository.save(otpEntity);

        emailService.sendOtp(employee.getEmail(), otp);
    }
    public LoginResponseDto verifyOtp(OtpRequestDto request) {

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        OtpVerification otpEntity =
                otpRepository.findTopByEmployeeOrderByCreatedAtDesc(employee)
                        .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otpEntity.isVerified())
            throw new RuntimeException("OTP already used");

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("OTP expired");

        if (otpEntity.getAttemptCount() >= 3)
            throw new RuntimeException("Too many attempts");

        boolean matches = passwordEncoder.matches(
                request.getOtp(),
                otpEntity.getOtpHash()
        );

        if (!matches) {
            otpEntity.setAttemptCount(otpEntity.getAttemptCount() + 1);
            otpRepository.save(otpEntity);
            throw new RuntimeException("Invalid OTP");
        }

        // ✅ SUCCESS → mark as verified AND expired
        otpEntity.setVerified(true);
        otpEntity.setExpiryTime(LocalDateTime.now());  // Force expire immediately
        otpRepository.save(otpEntity);

        String token = jwtService.generateToken(employee);

        EmployeeDto employeeDto = modelService.convertToDto(employee);

        return LoginResponseDto.builder()
                .token(token)
                .employee(employeeDto)
                .build();
    }
    public void resendOtp(String email) {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OtpVerification lastOtp = otpRepository
                .findTopByEmployeeOrderByCreatedAtDesc(employee)
                .orElseThrow(() -> new RuntimeException("No OTP found. Please login again."));

        // 🔐 If OTP still active → block resend
        if (!lastOtp.isVerified() &&
                lastOtp.getExpiryTime().isAfter(LocalDateTime.now())) {

            long secondsRemaining =
                    java.time.Duration.between(
                            LocalDateTime.now(),
                            lastOtp.getExpiryTime()
                    ).getSeconds();

            throw new RuntimeException(
                    "OTP already sent. Try again after " + secondsRemaining + " seconds."
            );
        }

        // ✅ If expired or verified → generate new OTP
        generateAndSendOtp(email);
    }
}