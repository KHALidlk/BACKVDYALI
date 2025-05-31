package org.example.ebanking1.controller;

import org.example.ebanking1.entities.BankingProduct;
import org.example.ebanking1.service.BankingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/banking-products")
public class BankingProductController {

    private final BankingProductService service;

    @Autowired
    public BankingProductController(BankingProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BankingProduct> save(@RequestBody BankingProduct entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankingProduct> findById(@PathVariable String id) {
        Optional<BankingProduct> product = service.findById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<BankingProduct>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<BankingProduct>> findActiveProducts() {
        return ResponseEntity.ok(service.findActiveProducts());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<BankingProduct>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<BankingProduct>> findByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.findByCategory(category));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<BankingProduct> activateProduct(@PathVariable String id) {
        try {
            BankingProduct product = service.activateProduct(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<BankingProduct> deactivateProduct(@PathVariable String id) {
        try {
            BankingProduct product = service.deactivateProduct(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
