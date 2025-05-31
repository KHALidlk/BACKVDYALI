package org.example.ebanking1.controller;

import org.example.ebanking1.entities.Announcement;
import org.example.ebanking1.service.AnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/announcements")
public class AnnouncementController {

    private final AnnouncementService service;

    @Autowired
    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Announcement> save(@RequestBody Announcement entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Announcement> findById(@PathVariable String id) {
        Optional<Announcement> announcement = service.findById(id);
        return announcement.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Announcement>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/important")
    public ResponseEntity<List<Announcement>> findImportantAnnouncements() {
        return ResponseEntity.ok(service.findImportantAnnouncements());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Announcement>> findByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Announcement>> findByDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(service.findByDateBetween(start, end));
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<List<Announcement>> findByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(service.findByAuthor(author));
    }

    @GetMapping("/current")
    public ResponseEntity<List<Announcement>> findCurrentAnnouncements() {
        return ResponseEntity.ok(service.findCurrentAnnouncements());
    }
}
