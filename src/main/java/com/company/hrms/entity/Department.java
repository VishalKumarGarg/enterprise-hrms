package com.company.hrms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Department name must be unique (e.g., IT, HR, Finance)
    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // NEW → Ensures every department has creation timestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // One department can have multiple employees
    // mappedBy = "department" refers to the field in Employee entity
    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    // NEW → Automatically set createdAt before inserting into database
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}