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
@Table(name = "transfers")
public class Transfermodel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    @JsonBackReference(value = "sender-transfer")
    private StripeAccount senderAccount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    @JsonBackReference(value = "recipient-transfer")
    private StripeAccount recipientAccount;

    private BigDecimal amount;
    private String currency;
    private String description;
    private String reference;
    private String status; // pending, completed, failed, cancelled

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;

    @Column(name = "processing_date")
    private LocalDateTime processingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
