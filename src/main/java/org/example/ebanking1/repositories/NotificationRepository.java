package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Notification;
import org.example.ebanking1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    List<Notification> findByUser(User user);
    List<Notification> findByUserAndIsReadFalse(User user);
    List<Notification> findByUserAndCategory(User user, String category);
    List<Notification> findByUserAndExpiresAtAfter(User user, LocalDateTime date);
    List<Notification> findByType(String type);
}