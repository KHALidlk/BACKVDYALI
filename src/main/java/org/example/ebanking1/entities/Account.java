package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    private String type; // current, savings, investment, etc.
    private BigDecimal balance;

    @Column(name = "available_balance")
    private BigDecimal availableBalance;
@Column(name = "currency")
@org.hibernate.annotations.ColumnDefault("'EUR'") // Default to Euro currency
private String currency;
    private String status; // active, inactive, blocked, pending
    @Column(name = "opened_date")
    private LocalDateTime openedDate;

    @Column(name = "last_transaction_date")
    private LocalDateTime lastTransactionDate;
private String rib;
    private String iban;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "monthly_limit")
    private BigDecimal monthlyLimit;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships - removed recharges relationship
    @OneToOne(mappedBy = "account")
    @JsonManagedReference
    private AccountBankNumber accountBankNumber;

//    @OneToMany(mappedBy = "sourceAccount")
//    @JsonManagedReference
//    private List<Transaction> outgoingTransactions;
//
//    @OneToMany(mappedBy = "destinationAccount")
//    @JsonManagedReference
//    private List<Transaction> incomingTransactions;
//
//    @OneToMany(mappedBy = "senderAccount")
//    @JsonManagedReference
//    private List<Transfer> outgoingTransfers;
//
//    @OneToMany(mappedBy = "recipientAccount")
//    @JsonManagedReference
//    private List<Transfer> incomingTransfers;

    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Card> cards;
    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Document> documents;
//
//    @OneToMany(mappedBy = "account")
//    @JsonManagedReference
//    private List<BillPayment> billPayments;
//
//    @OneToMany(mappedBy = "account")
//    @JsonManagedReference
//    private List<CardRequest> cardRequests;
}