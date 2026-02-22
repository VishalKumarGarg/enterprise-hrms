package com.company.hrms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "office_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Office name (e.g., Head Office, Delhi Branch)
    @Column(nullable = false, unique = true)
    private String officeName;

    // Latitude of office for geo-fencing validation
    @Column(nullable = false)
    private Double latitude;

    // Longitude of office for geo-fencing validation
    @Column(nullable = false)
    private Double longitude;

    // Allowed radius in meters (e.g., 200 meters)
    @Column(nullable = false)
    private Double allowedRadius;

    // Public IP of office router (for network validation)
    @Column(nullable = false)
    private String publicIp;

    // Office active status (Admin can disable branch)
    @Column(nullable = false)
    private Boolean active = true;

    // Creation timestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Automatically set createdAt before saving
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}