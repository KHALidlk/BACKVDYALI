package org.example.ebanking1.controller;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.entities.Document;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService service;

    @Autowired
    public DocumentController(DocumentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Document> save(@RequestBody Document entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }
    @GetMapping("/relverBancair")
    public Document relverBancair(@RequestParam String accountid, @RequestParam String clientid) {
        return service.relverBancair(accountid, clientid);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> findById(@PathVariable String id) {
        Optional<Document> document = service.findById(id);
        return document.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Document>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<Document>> findByUser(@RequestBody User user) {
        return ResponseEntity.ok(service.findByUser(user));
    }

    @GetMapping("/account")
    public ResponseEntity<List<Document>> findByAccount(@RequestBody Account account) {
        return ResponseEntity.ok(service.findByAccount(account));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Document>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Document>> findUnreadDocuments() {
        return ResponseEntity.ok(service.findUnreadDocuments());
    }

    @GetMapping("/archived")
    public ResponseEntity<List<Document>> findArchivedDocuments() {
        return ResponseEntity.ok(service.findArchivedDocuments());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<Document>> findByCreatedAtBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(service.findByCreatedAtBetween(start, end));
    }

    @PutMapping("/{id}/mark-as-read")
    public ResponseEntity<Document> markAsRead(@PathVariable String id) {
        try {
            Document document = service.markAsRead(id);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<Document> archiveDocument(@PathVariable String id) {
        try {
            Document document = service.archiveDocument(id);
            return ResponseEntity.ok(document);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
