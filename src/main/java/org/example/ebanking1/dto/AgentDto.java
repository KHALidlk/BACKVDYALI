package org.example.ebanking1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentDto {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String employeeId;
    private String branch;
    private String role;
    private String status;
    private String imageUrl;
    private LocalDateTime dateJoined;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // User information
    private String userId;
    private String username;
    private String userEmail;
}
