package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, String> {
    List<Announcement> findByIsImportantTrue();
    List<Announcement> findByCategory(String category);
    List<Announcement> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Announcement> findByAuthor(String author);
}