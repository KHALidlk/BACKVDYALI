package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Document;
import org.example.ebanking1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, String> {
    List<Document> findByUser(User user);
    List<Document> findByAccount(Account account);
    List<Document> findByType(String type);
    List<Document> findByIsReadFalse();
    List<Document> findByIsArchivedTrue();
    List<Document> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}