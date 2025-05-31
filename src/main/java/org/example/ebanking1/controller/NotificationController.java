package org.example.ebanking1.controller;

import org.example.ebanking1.entities.Notification;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Notification> save(@RequestBody Notification entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> findById(@PathVariable String id) {
        Optional<Notification> notification = service.findById(id);
        return notification.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Notification>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user")
    public ResponseEntity<List<Notification>> findByUser(@RequestBody User user) {
        return ResponseEntity.ok(service.findByUser(user));
    }

    @PostMapping("/user/unread")
    public ResponseEntity<List<Notification>> findUnreadByUser(@RequestBody User user) {
        return ResponseEntity.ok(service.findUnreadByUser(user));
    }

    @PostMapping("/user/category")
    public ResponseEntity<List<Notification>> findByUserAndCategory(
            @RequestBody User user,
            @RequestParam String category) {
        return ResponseEntity.ok(service.findByUserAndCategory(user, category));
    }

    @PostMapping("/user/active")
    public ResponseEntity<List<Notification>> findActiveByUser(@RequestBody User user) {
        return ResponseEntity.ok(service.findActiveByUser(user));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Notification>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @PutMapping("/{id}/mark-as-read")
    public ResponseEntity<Notification> markAsRead(@PathVariable String id) {
        try {
            Notification notification = service.markAsRead(id);
            return ResponseEntity.ok(notification);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/user/mark-all-as-read")
    public ResponseEntity<Void> markAllAsRead(@RequestBody User user) {
        service.markAllAsRead(user);
        return ResponseEntity.ok().build();
    }
}
