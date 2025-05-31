package org.example.ebanking1.controller;

import org.example.ebanking1.entities.RechargeProvider;
import org.example.ebanking1.service.RechargeProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recharge-providers")
public class RechargeProviderController {

    private final RechargeProviderService rechargeProviderService;

    public RechargeProviderController(RechargeProviderService rechargeProviderService) {
        this.rechargeProviderService = rechargeProviderService;
    }

    @GetMapping
    public ResponseEntity<List<RechargeProvider>> getAllProviders() {
        return ResponseEntity.ok(rechargeProviderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProviderById(@PathVariable String id) {
        return rechargeProviderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RechargeProvider> createProvider(@RequestBody RechargeProvider provider) {
        RechargeProvider savedProvider = rechargeProviderService.save(provider);
        return new ResponseEntity<>(savedProvider, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvider(@PathVariable String id, @RequestBody RechargeProvider provider) {
        return rechargeProviderService.findById(id)
                .map(existingProvider -> {
                    provider.setId(id);
                    return ResponseEntity.ok(rechargeProviderService.save(provider));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable String id) {
        rechargeProviderService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<RechargeProvider>> getActiveProviders() {
        return ResponseEntity.ok(rechargeProviderService.findActiveProviders());
    }

    @GetMapping("/service-type/{type}")
    public ResponseEntity<List<RechargeProvider>> getProvidersByServiceType(@PathVariable String type) {
        return ResponseEntity.ok(rechargeProviderService.findByServiceType(type));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<?> activateProvider(@PathVariable String id) {
        try {
            RechargeProvider provider = rechargeProviderService.activateProvider(id);
            return ResponseEntity.ok(provider);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateProvider(@PathVariable String id) {
        try {
            RechargeProvider provider = rechargeProviderService.deactivateProvider(id);
            return ResponseEntity.ok(provider);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}