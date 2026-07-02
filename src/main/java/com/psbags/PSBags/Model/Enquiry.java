package com.psbags.PSBags.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "enquiries")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "mobile", nullable = false, length = 15)
    private String mobile;

    @Column(name = "company_name", nullable = false, length = 200)
    private String companyName;

    @Column(name = "product_requirement", nullable = false, length = 500)
    private String productRequirement;

    @Column(name = "location", nullable = false, length = 200)
    private String location;

    @Column(name = "product_type", nullable = false, length = 100)
    private String productType;

    @Column(name = "product_count", nullable = false, length = 50)
    private String productCount;

    @Column(name = "status", nullable = false, length = 50)
    private String status = "NEW";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "NEW";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
