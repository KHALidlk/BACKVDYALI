package org.example.ebanking1.dto;

import org.example.ebanking1.entities.StripeAccount;
import org.example.ebanking1.entities.User;
import com.stripe.model.Account;

/**
 * Mapper utility class for converting between Stripe-related entities and DTOs
 */
public class StripeMapper {

    /**
     * Convert StripeAccount entity to StripeConnectDTO
     */
    public static StripeConnectDTO toDTO(StripeAccount stripeAccount) {
        if (stripeAccount == null) {
            return null;
        }

        StripeConnectDTO dto = new StripeConnectDTO();
        dto.setStripeAccountId(stripeAccount.getId());
        dto.setEmail(stripeAccount.getEmail());
        dto.setCountry(stripeAccount.getCountry());
        dto.setOnboarding_url(stripeAccount.getOnboarding_url());
        dto.setAccountType(stripeAccount.getType());

        // Set user information if available
        if (stripeAccount.getLocalAccount() != null) {
            dto.setUserId(stripeAccount.getLocalAccount().getId());
            dto.setClientId(stripeAccount.getLocalAccount().getId()); // Assuming User has clientId
        }

        return dto;
    }

    /**
     * Convert Stripe API Account to StripeConnectDTO
     */
    public static StripeConnectDTO toDTO(Account stripeApiAccount, String userId, String onboardingUrl) {
        if (stripeApiAccount == null) {
            return null;
        }

        StripeConnectDTO dto = new StripeConnectDTO();
        dto.setStripeAccountId(stripeApiAccount.getId());
        dto.setEmail(stripeApiAccount.getEmail());
        dto.setCountry(stripeApiAccount.getCountry());
        dto.setAccountType(stripeApiAccount.getType());
        dto.setOnboarding_url(onboardingUrl);
        dto.setUserId(userId);

        return dto;
    }

    /**
     * Create StripeAccount entity from StripeConnectDTO and User
     */
    public static StripeAccount toEntity(StripeConnectDTO dto, User user) {
        if (dto == null) {
            return null;
        }

        StripeAccount stripeAccount = new StripeAccount();
        stripeAccount.setId(dto.getStripeAccountId());
        stripeAccount.setEmail(dto.getEmail());
        stripeAccount.setCountry(dto.getCountry());
        stripeAccount.setOnboarding_url(dto.getOnboarding_url());
        stripeAccount.setType(dto.getAccountType());
        stripeAccount.setLocalAccount(user);

        // Set default values for new accounts
        stripeAccount.setChargesEnabled(false);
        stripeAccount.setPayoutsEnabled(false);
        stripeAccount.setDetailsSubmitted(false);

        return stripeAccount;
    }

    /**
     * Update StripeAccount entity from Stripe API Account
     */
    public static StripeAccount updateFromStripeApi(StripeAccount existingAccount, Account stripeApiAccount) {
        if (existingAccount == null || stripeApiAccount == null) {
            return existingAccount;
        }

        existingAccount.setEmail(stripeApiAccount.getEmail());
        existingAccount.setCountry(stripeApiAccount.getCountry());
        existingAccount.setChargesEnabled(stripeApiAccount.getChargesEnabled());
        existingAccount.setPayoutsEnabled(stripeApiAccount.getPayoutsEnabled());
        existingAccount.setDetailsSubmitted(stripeApiAccount.getDetailsSubmitted());
        existingAccount.setType(stripeApiAccount.getType());

        return existingAccount;
    }

    /**
     * Create a basic StripeConnectDTO for account creation
     */
    public static StripeConnectDTO createAccountCreationDTO(String userId, String email, String country) {
        StripeConnectDTO dto = new StripeConnectDTO();
        dto.setUserId(userId);
        dto.setEmail(email);
        dto.setCountry(country != null ? country : "FR"); // Default to France
        dto.setAccountType("EXPRESS"); // Default to Express account

        return dto;
    }

    /**
     * Create a response DTO after successful account creation
     */
    public static StripeConnectDTO createSuccessResponse(String stripeAccountId, String onboardingUrl, String userId) {
        StripeConnectDTO dto = new StripeConnectDTO();
        dto.setStripeAccountId(stripeAccountId);
        dto.setOnboarding_url(onboardingUrl);
        dto.setUserId(userId);
        dto.setAccountType("EXPRESS");

        return dto;
    }
}