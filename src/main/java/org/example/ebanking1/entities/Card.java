package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    private String type; // debit, credit, prepaid, virtual
    private String network; // visa, mastercard, amex, other

    @Column(name = "cardholder_name")
    private String cardholderName;

    @Column(name = "masked_number")
    private String maskedNumber;

    @Column(name = "expiry_month")
    private String expiryMonth;

    @Column(name = "expiry_year")
    private String expiryYear;

    private String status; // active, inactive, blocked, expired

    @Column(name = "is_contactless")
    private Boolean isContactless;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "online_payment_enabled")
    private Boolean onlinePaymentEnabled;

    @Column(name = "international_payment_enabled")
    private Boolean internationalPaymentEnabled;

    @Column(name = "pin_locked")
    private Boolean pinLocked;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "block_reason")
    private String blockReason;
}
