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
@Table(name = "alert_settings")
public class AlertSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    @Column(name = "balance_below")
    private BigDecimal balanceBelow;

    @Column(name = "large_transactions")
    private BigDecimal largeTransactions;

    @Column(name = "login_new_device")
    private Boolean loginNewDevice;

    @Column(name = "suspicious_activity")
    private Boolean suspiciousActivity;

    @Column(name = "new_statement")
    private Boolean newStatement;

    @Column(name = "payment_due")
    private Boolean paymentDue;

    @Column(name = "email_enabled")
    private Boolean emailEnabled;

    @Column(name = "sms_enabled")
    private Boolean smsEnabled;

    @Column(name = "push_enabled")
    private Boolean pushEnabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}