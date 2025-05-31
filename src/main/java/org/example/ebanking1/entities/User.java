package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true)
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "password_salt")
    private String passwordSalt;

    private String role; // client, agent, admin

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
    @Column(name = "phone_number")
    private String phoneNumber;

    private String language;

    @Column(name = "gdpr_consent")
    private Boolean gdprConsent;

    @Column(name = "gdpr_consent_date")
    private LocalDateTime gdprConsentDate;

    @Column(name = "stripe_connect_id")
    private String stripeConnectId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "identity_type")
    private String identityType;

    @Column(name = "identity_number")
    private String identityNumber;

    @Column(name = "two_factor_enabled")
    private Boolean twoFactorEnabled;

    @Column(name = "two_factor_method")
    private String twoFactorMethod;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    // Relationships
    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Client> clients;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Agent> agents;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Notification> notifications;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnoreProperties("user")
//    private List<AuthenticationLog> authenticationLogs;
//
//    @OneToMany(mappedBy = "user")
//    @JsonIgnoreProperties("user")
//    private List<SecurityLog> securityLogs;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnoreProperties("user")
//    private List<ChatLog> chatLogs;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<PaymentMethod> paymentMethods;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Document> documents;

//    @OneToMany(mappedBy = "user")
//    @JsonIgnoreProperties("user")
//    private List<Budget> budgets;
}

