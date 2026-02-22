package com.company.hrms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "payroll",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "month", "year"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Month and Year of payroll
    @Column(nullable = false)
    private int month;

    @Column(nullable = false)
    private int year;

    // Total working days in that month
    @Column(nullable = false)
    private int totalWorkingDays;

    @Column(nullable = false)
    private int presentDays;

    @Column(nullable = false)
    private int absentDays;

    @Column(nullable = false)
    private int halfDays;

    // Base salary at the time of payroll generation
    @Column(nullable = false)
    private double baseSalary;

    // Total deductions calculated
    @Column(nullable = false)
    private double deductions;

    // Final salary after calculation
    @Column(nullable = false)
    private double finalSalary;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    // Payroll belongs to one employee
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @PrePersist
    public void prePersist() {
        this.generatedDate = LocalDateTime.now();
    }
}