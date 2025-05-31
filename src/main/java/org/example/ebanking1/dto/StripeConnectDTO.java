package org.example.ebanking1.dto;

import lombok.Data;

/**
 * Data Transfer Object for Stripe Connect Account
 */
@Data
public class StripeConnectDTO {
    private String userId;
    private String clientId;
    private String email;
    private String country;
    private String onboarding_url; // 'individual', 'company', etc.
    private String accountType; // 'standard', 'express', or 'custom'
    private String redirectUrl; // URL to redirect after account creation/connection
    private String stripeAccountId; // Stripe account ID once connected
}
