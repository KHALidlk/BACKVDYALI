package org.example.ebanking1.controller;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.AccountBankNumber;
import org.example.ebanking1.service.AccountBankNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/account-bank-numbers")
public class AccountBankNumberController {

    private final AccountBankNumberService service;

    @Autowired
    public AccountBankNumberController(AccountBankNumberService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountBankNumber> save(@RequestBody AccountBankNumber entity) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(entity));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountBankNumber> findById(@PathVariable String id) {
        try {
            Optional<AccountBankNumber> accountBankNumber = service.findById(id);
            return accountBankNumber.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AccountBankNumber>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | jakarta.persistence.EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/generate-rib")
    public ResponseEntity<String> generateRIB(@RequestBody AccountBankNumber accountBankNumber) {
        try {
            String rib = service.generateRIB(accountBankNumber);
            return ResponseEntity.ok(rib);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/generate-iban")
    public ResponseEntity<String> generateIBAN(@RequestBody AccountBankNumber accountBankNumber) {
        try {
            String iban = service.generateIBAN(accountBankNumber);
            return ResponseEntity.ok(iban);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/create-for-account")
    public ResponseEntity<AccountBankNumber> createAccountBankNumber(@RequestBody Account account) {
        try {
            AccountBankNumber accountBankNumber = service.createAccountBankNumber(account);
            return ResponseEntity.status(HttpStatus.CREATED).body(accountBankNumber);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<AccountBankNumber>> findByAccountId(@PathVariable String accountId) {
        List<AccountBankNumber> numbers = service.findByAccountId(accountId);
        return ResponseEntity.ok(numbers);
    }
}
