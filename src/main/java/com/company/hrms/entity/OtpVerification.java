package com.company.hrms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hashed OTP (never store plain OTP)
    @Column(nullable = false)
    private String otpHash;

    // OTP expiry time (e.g., now + 5 minutes)
    @Column(nullable = false)
    private LocalDateTime expiryTime;

    // Track failed attempts (max 3 allowed)
    @Column(nullable = false)
    private int attemptCount = 0;

    // Mark OTP as verified
    @Column(nullable = false)
    private boolean verified = false;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // OTP belongs to one employee
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}