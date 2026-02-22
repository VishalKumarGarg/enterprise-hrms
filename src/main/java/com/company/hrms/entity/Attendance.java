package com.company.hrms.entity;

import com.company.hrms.enums.AttendanceStatus;
import com.company.hrms.enums.WorkMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(
    name = "attendance",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "date"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Attendance date (only one record per employee per day)
    @Column(nullable = false)
    private LocalDate date;

    // Check-in & check-out time
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    // Work mode for that specific day (WFO / WFH)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkMode workMode;

    // PRESENT, ABSENT, HALF_DAY, LATE
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    // Employee's latitude and longitude during check-in
    private Double latitude;
    private Double longitude;

    // Store IP address used during attendance marking
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Many attendance records belong to one employee
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}