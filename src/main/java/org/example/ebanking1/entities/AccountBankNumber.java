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
@Table(name = "account_bank_numbers")
public class AccountBankNumber {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "check_code")
    private String checkCode;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "rib_key")
    private String ribKey;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}