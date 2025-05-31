package org.example.ebanking1.controller;

import org.example.ebanking1.entities.Beneficiary;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.service.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService service;

    @Autowired
    public BeneficiaryController(BeneficiaryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Beneficiary> save(@RequestBody Beneficiary entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> findById(@PathVariable String id) {
        Optional<Beneficiary> beneficiary = service.findById(id);
        return beneficiary.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Beneficiary>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/client")
    public ResponseEntity<List<Beneficiary>> findByClient(@RequestBody Client client) {
        return ResponseEntity.ok(service.findByClient(client));
    }

    @PostMapping("/client/favorites")
    public ResponseEntity<List<Beneficiary>> findFavoritesByClient(@RequestBody Client client) {
        return ResponseEntity.ok(service.findFavoritesByClient(client));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<Beneficiary>> findByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(service.findByAccountNumber(accountNumber));
    }

    @GetMapping("/bank/{bankCode}")
    public ResponseEntity<List<Beneficiary>> findByBankCode(@PathVariable String bankCode) {
        return ResponseEntity.ok(service.findByBankCode(bankCode));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Beneficiary>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @PutMapping("/{id}/mark-favorite")
    public ResponseEntity<Beneficiary> markAsFavorite(@PathVariable String id) {
        try {
            Beneficiary beneficiary = service.markAsFavorite(id);
            return ResponseEntity.ok(beneficiary);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/unmark-favorite")
    public ResponseEntity<Beneficiary> unmarkAsFavorite(@PathVariable String id) {
        try {
            Beneficiary beneficiary = service.unmarkAsFavorite(id);
            return ResponseEntity.ok(beneficiary);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
