package com.company.hrms.entity;

import com.company.hrms.enums.LeaveStatus;
import com.company.hrms.enums.LeaveType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // SICK, CASUAL, EARNED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String reason;

    // PENDING, APPROVED, REJECTED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaveStatus status;

    // Employee who applied leave
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Manager/HR who approved leave
    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Employee approvedBy;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime decisionDate;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = LeaveStatus.PENDING;
    }
}