package com.company.hrms.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDto {

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String email;

    private String role;
    private String status;
    private String workMode;

    private Double salary; // optional – remove if sensitive

    private String departmentName;
    private Long departmentId;

    private String managerName;
    private Long managerId;

    private LocalDateTime createdAt;
}