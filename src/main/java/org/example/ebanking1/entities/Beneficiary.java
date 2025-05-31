package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "beneficiaries")
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    private String name;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    private String category;
    private String reference;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String type; // domestic, international, internal
}