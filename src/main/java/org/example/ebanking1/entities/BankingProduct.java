package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "banking_products")
public class BankingProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String type; // account, loan, investment, insurance, card
    private String description;
    private String features;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "minimum_deposit")
    private BigDecimal minimumDeposit;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "image_url")
    private String imageUrl;

    private String requirements;

    @Column(name = "terms_url")
    private String termsUrl;

    private String category;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductApplication> applications;
}