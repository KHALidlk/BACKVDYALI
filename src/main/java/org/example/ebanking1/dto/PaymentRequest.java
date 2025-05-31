package org.example.ebanking1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentRequest {

    @NotNull(message = "Le montant est obligatoire")
    @Min(value = 1, message = "Le montant doit être au minimum de 1")
    private Double amount;

    @NotBlank(message = "La devise est obligatoire")
    private String currency;

    // Dans Stripe, on doit indiquer le type de paiement
    @NotBlank(message = "Le type de méthode de paiement est obligatoire")
    private String paymentMethodType;

    public PaymentRequest() {
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }
}
