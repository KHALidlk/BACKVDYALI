package org.example.ebanking1.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.AccountLink;
import com.stripe.model.Balance;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import jakarta.annotation.PostConstruct;
import org.example.ebanking1.entities.StripeAccount;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.Striperepo;
import org.example.ebanking1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StripeConnectService {

    private final UserRepository userRepository;
    private final Striperepo stripeRepository;

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @Value("${stripe.connect.return.url}")
    private String returnUrl;

    @Value("${stripe.connect.refresh.url}")
    private String refreshUrl;

    @Autowired
    public StripeConnectService(UserRepository userRepository, Striperepo stripeRepository) {
        this.userRepository = userRepository;
        this.stripeRepository = stripeRepository;
    }

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Map<String, String> createStripeAccountForUser(String userId) throws StripeException {
        // 1. Récupérer l'utilisateur existant depuis la base
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Vérifier si l'utilisateur a déjà un compte Stripe
        if (existingUser.getStripeConnectId() != null) {
            throw new RuntimeException("User already has a Stripe account");
        }

        // 2. Créer un compte Stripe Express
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setCountry("FR")
                .setEmail(existingUser.getEmail())
                .setCapabilities(AccountCreateParams.Capabilities.builder()
                        .setTransfers(AccountCreateParams.Capabilities.Transfers.builder()
                                .setRequested(true).build())
                        .setCardPayments(AccountCreateParams.Capabilities.CardPayments.builder()
                                .setRequested(true).build())
                        .build())
                .build();

        // Créer le compte Stripe AVANT de créer le lien
        com.stripe.model.Account stripeAccount = com.stripe.model.Account.create(params);

        // 3. Créer l'URL d'onboarding APRÈS avoir créé le compte
        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(stripeAccount.getId())
                .setRefreshUrl(refreshUrl) // Définir ces variables ou utiliser des valeurs hardcodées
                .setReturnUrl(returnUrl)
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(linkParams);

        // 4. Mettre à jour l'utilisateur existant avec l'ID Stripe
        existingUser.setStripeConnectId(stripeAccount.getId());
        User updatedUser = userRepository.save(existingUser);

        // 5. Créer l'entité StripeAccount locale
        StripeAccount sa = new StripeAccount();
        sa.setId(stripeAccount.getId());
        sa.setLocalAccount(updatedUser);
        sa.setEmail(stripeAccount.getEmail());
        sa.setCountry(stripeAccount.getCountry());
        sa.setChargesEnabled(stripeAccount.getChargesEnabled());
        sa.setPayoutsEnabled(stripeAccount.getPayoutsEnabled());
        sa.setDetailsSubmitted(stripeAccount.getDetailsSubmitted());
        sa.setOnboarding_url(accountLink.getUrl());
        sa.setType(stripeAccount.getType());
        stripeRepository.save(sa);

        // 6. Retourner les informations importantes
        return Map.of(
                "stripe_account_id", stripeAccount.getId(),
                "onboarding_url", accountLink.getUrl()
        );
    }

    public Map<String, String> refreshOnboardingLink(String userId) throws StripeException {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (existingUser.getStripeConnectId() == null) {
            throw new RuntimeException("User does not have a Stripe account");
        }

        AccountLinkCreateParams linkParams = AccountLinkCreateParams.builder()
                .setAccount(existingUser.getStripeConnectId())
                .setRefreshUrl(refreshUrl)
                .setReturnUrl(returnUrl)
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(linkParams);

        return Map.of(
                "stripe_account_id", existingUser.getStripeConnectId(),
                "onboarding_url", accountLink.getUrl()
        );
    }

    public List<StripeAccount> find() {
        return stripeRepository.findAll();
    }

    public StripeAccount findByUserId(String userId) {
        try {
            List<StripeAccount> accounts = stripeRepository.findByLocalAccountId(userId);
            if (accounts != null && !accounts.isEmpty()) {
                return accounts.get(0);
            }
            return null;
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error finding StripeAccount by userId: " + e.getMessage());
            return null;
        }
    }

    public Balance getStripeBalanceOfConnectedAccount(String connectedAccountId) throws StripeException {
        RequestOptions requestOptions = RequestOptions.builder()
                .setStripeAccount(connectedAccountId)
                .build();
        return Balance.retrieve(requestOptions);
    }
}

