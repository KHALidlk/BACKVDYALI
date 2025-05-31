package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    private String phone;

    @Column(name = "client_id", unique = true)
    private String clientId;

    @Column(name = "identity_number")
    private String identityNumber;

    private String address;
    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;
    private String status; // active, inactive, blocked, pending

    @Column(name = "account_type")
    private String accountType; // personal, business, premium

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "date_joined")
    private LocalDateTime dateJoined;

    @Column(name = "verification_date")
    private LocalDateTime verificationDate;

    @Column(name = "otp_secret")
    private String otpSecret;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<Account> accounts;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<Beneficiary> beneficiaries;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<AlertSetting> alertSettings;

//    @OneToMany(mappedBy = "client")
//    @JsonManagedReference
//    private List<Bill> bills;

    @OneToMany(mappedBy = "client")
    @JsonManagedReference
    private List<ProductApplication> productApplications;
//
//    @OneToMany(mappedBy = "client")
//    @JsonManagedReference
//    private List<Task> tasks;
//
//    @OneToMany(mappedBy = "referrer")
//    @JsonManagedReference
//    private List<Referral> referrals;
//
//    @OneToMany(mappedBy = "client")
//    @JsonManagedReference
//    private List<CardRequest> cardRequests;
}