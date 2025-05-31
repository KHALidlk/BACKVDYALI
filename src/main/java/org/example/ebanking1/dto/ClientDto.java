package org.example.ebanking1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private String id;
    private String userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String clientId;
    private String identityNumber;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String status;
    private String accountType;
    private String imageUrl;
    private LocalDateTime dateJoined;
    private LocalDateTime verificationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User information
    private String username;
    private String email;

    // Summary information about related entities
    private int numberOfAccounts;
    private int numberOfBeneficiaries;
    private List<AccountDTO> accounts;
}
