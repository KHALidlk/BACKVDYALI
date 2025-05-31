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
@Table(name = "recharge_providers")
public class RechargeProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Column(name = "service_type")
    private String serviceType; // mobile, internet, tv, gaming, streaming

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "fee_percentage")
    private BigDecimal feePercentage;

    @Column(name = "fixed_fee")
    private BigDecimal fixedFee;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    // Relationships - removed recharges relationship
//    @OneToMany(mappedBy = "provider")
//    @JsonManagedReference
//    private List<Bill> bills;
//
//    @OneToMany(mappedBy = "provider")
//    @JsonManagedReference
//    private List<Transaction> transactions;
}