package org.example.ebanking1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stripe_accounts")
public class StripeAccount {
    @Id
    private String id; // ex: acct_1Nv0FGQ9RKHgCVdK

    @Column(name = "email")
    private String email;

    @Column(name = "country")
    private String country;

    @Column(name = "charges_enabled")
    private Boolean chargesEnabled;

    @Column(name = "payouts_enabled")
    private Boolean payoutsEnabled;

    @Column(name = "details_submitted")
    private Boolean detailsSubmitted;

    @Column(name = "onboarding_url")
    private String onboarding_url;

    @Column(name = "account_type")
    private String type;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User localAccount; // Le lien avec ton compte local
}
