package org.example.ebanking1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
    private String sourceAccountId;  // The account ID where money is taken from
    private String destinationAccountId; // The account ID where money is sent to
    private String sourceUserId;  // The user who is sending the money
    private String destinationUserId; // The user who is receiving the money
    private BigDecimal amount;
    private String currency;
    private String description;
    private String reference;
}
