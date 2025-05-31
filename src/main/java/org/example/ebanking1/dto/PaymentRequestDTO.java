package org.example.ebanking1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private Long amount; // en centimes, ex: 5000 = 50 EUR
    private String currency; // "eur"
    private String destinationStripeAccountId; // ID Stripe Express du bénéficiaire
    private Long applicationFeeAmount; // frais plateforme en centimes (optionnel)
    private String sourceUserId; // Optional, can be provided in request parameter
}
