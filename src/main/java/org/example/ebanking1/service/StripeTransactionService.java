package org.example.ebanking1.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.Transfer;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.TransferCreateParams;
import com.stripe.net.RequestOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeTransactionService {

    @Value("${STRIPE_SECRET_KEY}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    /**
     * Creates a Stripe payment intent for transferring money
     * from a user to another Stripe Express account.
     *
     * @param amountInCents Amount in cents (e.g., 5000 = 50€)
     * @param currency Currency (e.g., "eur")
     * @param destinationStripeAccountId Destination Stripe account ID
     * @param applicationFeeAmount Platform fee in cents (optional)
     * @return Created PaymentIntent
     * @throws StripeException Stripe API exception
     */
    public PaymentIntent createTransaction(
            long amountInCents,
            String currency,
            String destinationStripeAccountId,
            Long applicationFeeAmount) throws StripeException {

        System.out.println("Creating payment intent in Stripe");
        System.out.println("Amount: " + amountInCents + " " + currency);
        System.out.println("Destination: " + destinationStripeAccountId);
        System.out.println("Fee: " + applicationFeeAmount);

        PaymentIntentCreateParams.Builder paramsBuilder = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .addPaymentMethodType("card")
                .setConfirm(true) // Auto-confirm the payment intent
                .setPaymentMethod("pm_card_visa") // Use test card for testing
                .setTransferData(
                        PaymentIntentCreateParams.TransferData.builder()
                                .setDestination(destinationStripeAccountId)
                                .build()
                );

        if (applicationFeeAmount != null && applicationFeeAmount > 0) {
            paramsBuilder.setApplicationFeeAmount(applicationFeeAmount);
        }

        PaymentIntentCreateParams params = paramsBuilder.build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            System.out.println("Payment intent created with ID: " + paymentIntent.getId());
            System.out.println("Payment intent status: " + paymentIntent.getStatus());
            return paymentIntent;
        } catch (StripeException e) {
            System.err.println("Stripe error creating payment intent: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Creates a direct transfer between Stripe accounts
     * This method handles transfers between connected accounts using the correct approach
     *
     * @param amountInCents Amount in cents to transfer
     * @param currency Currency code (e.g., "eur")
     * @param sourceAccountId Source Stripe account ID (where money comes from)
     * @param destinationAccountId Destination Stripe account ID
     * @param description Description of the transfer
     * @return The created Transfer
     * @throws StripeException Stripe API exception
     */
    public Transfer createDirectTransfer(
            long amountInCents,
            String currency,
            String sourceAccountId,
            String destinationAccountId,
            String description) throws StripeException {

        System.out.println("Creating direct transfer in Stripe between connected accounts");
        System.out.println("Amount: " + amountInCents + " " + currency);
        System.out.println("Source: " + sourceAccountId);
        System.out.println("Destination: " + destinationAccountId);

        try {
            // Create a transfer using the Source account's funds to the destination account
            Map<String, Object> transferParams = new HashMap<>();
            transferParams.put("amount", amountInCents);
            transferParams.put("currency", currency);
            transferParams.put("destination", destinationAccountId);
            transferParams.put("description", description != null ? description : "Transfer between accounts");

            // Create request options to execute this as the source account
            RequestOptions requestOptions = RequestOptions.builder()
                    .setStripeAccount(sourceAccountId)
                    .build();

            // Execute the transfer as the source account
            Transfer transfer = Transfer.create(transferParams, requestOptions);

            System.out.println("Transfer created with ID: " + transfer.getId());
            System.out.println("Transfer object: " + transfer.getObject());

            return transfer;
        } catch (StripeException e) {
            System.err.println("Stripe error creating transfer: " + e.getMessage());

            // If direct transfer failed, try with a different approach
            if (e.getMessage().contains("Cannot create transfers")) {
                System.out.println("Attempting alternative transfer method...");
                return createTransferWithPayout(amountInCents, currency, sourceAccountId, destinationAccountId, description);
            }

            throw e;
        }
    }

    /**
     * Alternative method to transfer funds between accounts using payouts
     * Used as fallback when direct transfers aren't possible
     */
    private Transfer createTransferWithPayout(
            long amountInCents,
            String currency,
            String sourceAccountId,
            String destinationAccountId,
            String description) throws StripeException {

        try {
            // 1. Create a payout from the source account to pull funds from it
            Map<String, Object> payoutParams = new HashMap<>();
            payoutParams.put("amount", amountInCents);
            payoutParams.put("currency", currency);
            payoutParams.put("method", "instant");
            payoutParams.put("description", "Funds withdrawal for transfer");

            RequestOptions sourceOptions = RequestOptions.builder()
                    .setStripeAccount(sourceAccountId)
                    .build();

            com.stripe.model.Payout payout = com.stripe.model.Payout.create(payoutParams, sourceOptions);
            System.out.println("Source account payout created: " + payout.getId());

            // 2. Now create a transfer to the destination account
            Map<String, Object> transferParams = new HashMap<>();
            transferParams.put("amount", amountInCents);
            transferParams.put("currency", currency);
            transferParams.put("destination", destinationAccountId);
            transferParams.put("description", description != null ? description : "Transfer between accounts");

            Transfer transfer = Transfer.create(transferParams);
            System.out.println("Transfer created with ID: " + transfer.getId());

            return transfer;
        } catch (StripeException e) {
            System.err.println("Stripe error in alternative transfer method: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Creates a direct transfer from the platform account to a connected account
     * Legacy method for backward compatibility
     */
    public Transfer createDirectTransfer(
            long amountInCents,
            String currency,
            String destinationAccountId,
            String description) throws StripeException {

        System.out.println("Creating direct transfer from platform in Stripe");
        System.out.println("Amount: " + amountInCents + " " + currency);
        System.out.println("Destination: " + destinationAccountId);

        TransferCreateParams transferParams = TransferCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency(currency)
                .setDestination(destinationAccountId)
                .setDescription(description != null ? description : "Transfer from platform")
                .build();

        try {
            Transfer transfer = Transfer.create(transferParams);
            System.out.println("Transfer created with ID: " + transfer.getId());
            System.out.println("Transfer object: " + transfer.getObject());
            return transfer;
        } catch (StripeException e) {
            System.err.println("Stripe error creating transfer: " + e.getMessage());
            throw e;
        }
    }
}
