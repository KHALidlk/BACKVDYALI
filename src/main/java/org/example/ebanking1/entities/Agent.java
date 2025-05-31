package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "agents")
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;
    private String phone;

    @Column(name = "employee_id")
    private String employeeId;

    private String branch;
    private String role; // Junior Agent, Senior Agent, etc.
    private String status; // active, inactive, pending

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "date_joined")
    private LocalDateTime dateJoined;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    // Relationships
//    @OneToMany(mappedBy = "agent")
//    @JsonManagedReference
//    private List<ChatLog> chatLogs;
//
//    @OneToMany(mappedBy = "assignedTo")
//    @JsonManagedReference
//    private List<Task> assignedTasks;
}