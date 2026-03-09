package com.company.hrms.repository;

import com.company.hrms.entity.Employee;
import com.company.hrms.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {

    // Get latest OTP of an employee
    Optional<OtpVerification> 
    findTopByEmployeeOrderByCreatedAtDesc(Employee employee);

    // Delete all expired OTPs

}