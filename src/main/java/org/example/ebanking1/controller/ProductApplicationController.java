package org.example.ebanking1.controller;

import org.example.ebanking1.entities.BankingProduct;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.entities.ProductApplication;
import org.example.ebanking1.service.ProductApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-applications")
public class ProductApplicationController {

    private final ProductApplicationService service;

    @Autowired
    public ProductApplicationController(ProductApplicationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductApplication> save(@RequestBody ProductApplication entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductApplication> findById(@PathVariable String id) {
        Optional<ProductApplication> application = service.findById(id);
        return application.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductApplication>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client")
    public ResponseEntity<List<ProductApplication>> findByClient(@RequestBody Client client) {
        return ResponseEntity.ok(service.findByClient(client));
    }

    @GetMapping("/product")
    public ResponseEntity<List<ProductApplication>> findByProduct(@RequestBody BankingProduct product) {
        return ResponseEntity.ok(service.findByProduct(product));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProductApplication>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<ProductApplication>> findBySubmittedDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(service.findBySubmittedDateBetween(start, end));
    }

    @GetMapping("/this-month")
    public ResponseEntity<List<ProductApplication>> findApplicationsSubmittedThisMonth() {
        return ResponseEntity.ok(service.findApplicationsSubmittedThisMonth());
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<Void> updateApplicationStatus(
            @PathVariable String id,
            @PathVariable String status) {
        try {
            service.updateApplicationStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
