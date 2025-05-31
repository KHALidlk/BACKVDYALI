package org.example.ebanking1.controller;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Card;
import org.example.ebanking1.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService service;

    @Autowired
    public CardController(CardService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Card> save(@RequestBody Card entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> findById(@PathVariable(name = "id") String id) {
        if (id == null || id.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        // Trim the ID to handle any accidental spaces
        String trimmedId = id.trim();

        Optional<Card> card = service.findById(trimmedId);
        return card.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Card>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/account")
    public ResponseEntity<List<Card>> findByAccount(@RequestBody Account account) {
        return ResponseEntity.ok(service.findByAccount(account));
    }
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Card>> findByAccountId(@PathVariable String accountId) {
        List<Card> cards = service.findByAccountId(accountId);
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/account/status")
    public ResponseEntity<List<Card>> findByAccountAndStatus(
            @RequestBody Account account,
            @RequestParam String status) {
        return ResponseEntity.ok(service.findByAccountAndStatus(account, status));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Card>> findByType(@PathVariable String type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    @GetMapping("/network/{network}")
    public ResponseEntity<List<Card>> findByNetwork(@PathVariable String network) {
        return ResponseEntity.ok(service.findByNetwork(network));
    }



    @GetMapping("/cardholder/{name}")
    public ResponseEntity<List<Card>> findByCardholderName(@PathVariable String name) {
        return ResponseEntity.ok(service.findByCardholderName(name));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Card> activateCard(@PathVariable String id) {
        try {
            Card card = service.activateCard(id);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Card> blockCard(
            @PathVariable String id,
            @RequestParam String reason) {
        try {
            Card card = service.blockCard(id, reason);
            return ResponseEntity.ok(card);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
