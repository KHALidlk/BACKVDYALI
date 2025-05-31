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
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private Account account;

    private String type; // statement, receipt, contract, tax, other
    private String title;
    private String description;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "is_read")
    private Boolean isRead;

    @Column(name = "is_archived")
    private Boolean isArchived;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "period_start")
    private LocalDateTime periodStart;

    @Column(name = "period_end")
    private LocalDateTime periodEnd;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "archived_date")
    private LocalDateTime archivedDate;

    private String message;

}