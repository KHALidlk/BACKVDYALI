package org.example.ebanking1.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.stripe.model.Transfer;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO for Stripe Transfer responses to avoid serialization issues with internal Stripe objects
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StripeTransferDTO {
    private String id;
    private String object;
    private Long amount;
    private String currency;
    private String destination;
    private Long created;
    private String description;
    private Map<String, Object> metadata;

    public StripeTransferDTO() {
    }

    public StripeTransferDTO(Transfer transfer) {
        if (transfer != null) {
            this.id = transfer.getId();
            this.object = transfer.getObject();
            this.amount = transfer.getAmount();
            this.currency = transfer.getCurrency();
            this.destination = transfer.getDestination();
            this.created = transfer.getCreated();
            this.description = transfer.getDescription();

            // Copy metadata safely to avoid serialization issues
            if (transfer.getMetadata() != null) {
                this.metadata = new HashMap<>(transfer.getMetadata());
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
