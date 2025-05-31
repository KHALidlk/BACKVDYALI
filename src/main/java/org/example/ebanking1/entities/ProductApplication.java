package org.example.ebanking1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_applications")
public class ProductApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private BankingProduct product;

    private String status; // draft, submitted, under_review, approved, rejected

    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "required_documents")
    private String requiredDocuments;

    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}