package org.example.ebanking1.service;

import org.example.ebanking1.entities.Notification;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository repository;

    @Autowired
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Notification save(Notification entity) {
        return repository.save(entity);
    }

    public Optional<Notification> findById(String id) {
        return repository.findById(id);
    }

    public List<Notification> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Notification> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Notification> findUnreadByUser(User user) {
        return repository.findByUserAndIsReadFalse(user);
    }

    public List<Notification> findByUserAndCategory(User user, String category) {
        return repository.findByUserAndCategory(user, category);
    }

    public List<Notification> findActiveByUser(User user) {
        return repository.findByUserAndExpiresAtAfter(user, LocalDateTime.now());
    }

    public List<Notification> findByType(String type) {
        return repository.findByType(type);
    }

    @Transactional
    public Notification markAsRead(String id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        return repository.save(notification);
    }

    @Transactional
    public void markAllAsRead(User user) {
        List<Notification> unreadNotifications = repository.findByUserAndIsReadFalse(user);
        LocalDateTime now = LocalDateTime.now();
        
        for (Notification notification : unreadNotifications) {
            notification.setIsRead(true);
            notification.setReadAt(now);
        }
        
        repository.saveAll(unreadNotifications);
    }
}
