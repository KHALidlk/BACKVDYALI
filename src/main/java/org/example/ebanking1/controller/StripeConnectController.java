package org.example.ebanking1.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Balance;
import com.stripe.net.RequestOptions;
import org.example.ebanking1.dto.StripeConnectDTO;
import org.example.ebanking1.dto.StripeMapper;
import org.example.ebanking1.dto.StripeBalanceDTO;
import org.example.ebanking1.entities.StripeAccount;
import org.example.ebanking1.service.StripeConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/stripe")
public class StripeConnectController {
    private final StripeConnectService stripeConnectService;

    @Autowired
    public StripeConnectController(StripeConnectService stripeConnectService) {
        this.stripeConnectService = stripeConnectService;
    }

    String connectedAccountId = null; // Remplacez par l'ID du compte connecté

    // ✅ Créer un compte Stripe Express et retourner le lien d'onboarding
    @PostMapping("/create-account/{userId}")
    public ResponseEntity<?> createStripeAccount(@PathVariable("userId") String userId) {
        try {
            Map<String, String> result = stripeConnectService.createStripeAccountForUser(userId);
            connectedAccountId = result.get("stripe_account_id");
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé : " + e.getMessage());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur Stripe : " + e.getMessage());
        }

    }

    @GetMapping
    public ResponseEntity<List<StripeConnectDTO>> findall() {
        List<StripeAccount> accounts = stripeConnectService.find();

        // Convert entities to DTOs to avoid lazy loading issues
        List<StripeConnectDTO> accountDtos = accounts.stream()
                .map(StripeMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(accountDtos);
    }

    @GetMapping("/byuserid/{userId}")
    public ResponseEntity<StripeConnectDTO> findbyuserID(@PathVariable String userId) {
        StripeAccount account = stripeConnectService.findByUserId(userId);
        // Convert entity to DTO to avoid lazy loading issues
        StripeConnectDTO accountDto = account == null ? null : StripeMapper.toDTO(account);

        return ResponseEntity.ok(accountDto);
    }


    // ✅ Récupérer le statut d'activation du compte Stripe (charges et transferts)
    @GetMapping("/success/{accountId}")
    public ResponseEntity<String> handleStripeSuccess(@PathVariable String accountId) {
        try {
            Account account = Account.retrieve(accountId);

            boolean isChargesEnabled = Boolean.TRUE.equals(account.getChargesEnabled());
            boolean isPayoutsEnabled = Boolean.TRUE.equals(account.getPayoutsEnabled());

            if (isChargesEnabled && isPayoutsEnabled) {
                return ResponseEntity.ok("✅ Compte Stripe activé avec succès !");
            } else {
                return ResponseEntity.ok("⚠️ Compte créé, mais pas encore totalement vérifié.");
            }

        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la récupération du compte Stripe : " + e.getMessage());
        }

    }

    // ✅ (Optionnel) Permet de régénérer un lien d'onboarding si l'utilisateur a quitté le processus
    @GetMapping("/refresh-link/{userId}")
    public ResponseEntity<?> refreshOnboardingLink(@PathVariable("userId") String userId) {
        try {
            Map<String, String> result = stripeConnectService.refreshOnboardingLink(userId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé : " + e.getMessage());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur Stripe : " + e.getMessage());
        }
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<?> getUserStripeBalance(@PathVariable String userId) {

        try {
            StripeAccount stripeAccount = stripeConnectService.findByUserId(userId);
            if (stripeAccount == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compte Stripe introuvable pour ce user.");
            }
            Balance balance = stripeConnectService.getStripeBalanceOfConnectedAccount(stripeAccount.getId());
            System.out.println("Connected account ID utilisé: " + connectedAccountId);
            System.out.println("Stripe response: " + balance);
            // Convert to DTO to avoid serialization issues
            StripeBalanceDTO balanceDTO = new StripeBalanceDTO(balance);
            return ResponseEntity.ok(balanceDTO);
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur Stripe : " + e.getMessage());
        }
    }
}
