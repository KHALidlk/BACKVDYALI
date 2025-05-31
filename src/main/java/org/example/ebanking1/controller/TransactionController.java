package org.example.ebanking1.controller;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Transfer;
import org.example.ebanking1.dto.PaymentRequestDTO;
import org.example.ebanking1.dto.StripeTransferDTO;
import org.example.ebanking1.service.StripeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    private final StripeTransactionService stripePaymentService;

    @Autowired
    public TransactionController(StripeTransactionService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> payToClient(@RequestBody PaymentRequestDTO request, @RequestParam(required = false) String currentUserId) {
        try {
            // If currentUserId is provided in the request params, use it as the sourceUserId
            if (currentUserId != null && !currentUserId.isEmpty()) {
                request.setSourceUserId(currentUserId);
            }

            // Log request details for debugging
            System.out.println("Processing payment request: " + request);
            System.out.println("Destination account: " + request.getDestinationStripeAccountId());
            System.out.println("Amount: " + request.getAmount() + " " + request.getCurrency());

            PaymentIntent paymentIntent = stripePaymentService.createTransaction(
                    request.getAmount(),
                    request.getCurrency(),
                    request.getDestinationStripeAccountId(),
                    request.getApplicationFeeAmount());

            System.out.println("Payment intent created: " + paymentIntent.getId());
            System.out.println("Payment intent status: " + paymentIntent.getStatus());

            // Return full payment intent details for debugging
            Map<String, Object> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            response.put("id", paymentIntent.getId());
            response.put("status", paymentIntent.getStatus());
            response.put("amount", paymentIntent.getAmount());
            response.put("currency", paymentIntent.getCurrency());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Stripe error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

    @PostMapping("/direct-transfer")
    public ResponseEntity<?> directTransferToAccount(@RequestBody Map<String, Object> request) {
        try {
            // Extract required parameters from the request
            String sourceAccountId = (String) request.get("sourceAccountId");
            String destinationAccountId = (String) request.get("destinationStripeAccountId");
            Long amount = ((Number) request.get("amount")).longValue();
            String currency = (String) request.get("currency");
            String description = (String) request.getOrDefault("description", "Transfer between accounts");

            // Log request details for debugging
            System.out.println("Processing direct transfer request");
            System.out.println("Source account: " + sourceAccountId);
            System.out.println("Destination account: " + destinationAccountId);
            System.out.println("Amount: " + amount + " " + currency);

            // Call the new direct transfer method that properly handles source account
            Transfer transfer = stripePaymentService.createDirectTransfer(
                    amount,
                    currency,
                    sourceAccountId,
                    destinationAccountId,
                    description);

            System.out.println("Transfer created: " + transfer.getId());

            // Convert to DTO to avoid serialization issues
            StripeTransferDTO transferDTO = new StripeTransferDTO(transfer);

            return ResponseEntity.ok(transferDTO);
        } catch (StripeException e) {
            System.err.println("Stripe error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Stripe error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }
}

