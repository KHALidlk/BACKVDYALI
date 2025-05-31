package org.example.ebanking1.controller;

import org.example.ebanking1.dto.AccountDTO;
import org.example.ebanking1.dto.AccountMapper;
import org.example.ebanking1.entities.Account;
import org.example.ebanking1.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(AccountMapper.toDTOList(accounts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id)
                .map(account -> ResponseEntity.ok(AccountMapper.toDTO(account)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        try {
            Account account = accountService.getAccountByAccountNumber(accountNumber);
            return ResponseEntity.ok(AccountMapper.toDTO(account));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<AccountDTO>> getAccountsByClient(@PathVariable String clientId) {
        List<Account> accounts = accountService.getAccountsByClient(clientId);
        return ResponseEntity.ok(AccountMapper.toDTOList(accounts));
    }

    @GetMapping("/client/{clientId}/active")
    public ResponseEntity<List<AccountDTO>> getActiveAccountsByClient(@PathVariable String clientId) {
        List<Account> accounts = accountService.getActiveAccountsByClient(clientId);
        return ResponseEntity.ok(AccountMapper.toDTOList(accounts));
    }

    @PostMapping("/{clientId}/{currencyId}")
    public ResponseEntity<AccountDTO> createAccount(
            @RequestBody Account account,
            @PathVariable String clientId,
            @PathVariable String currencyId) {
        Account createdAccount = accountService.createAccount(clientId, currencyId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(AccountMapper.toDTO(createdAccount));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable String id, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return ResponseEntity.ok(AccountMapper.toDTO(updatedAccount));
    }

    @PutMapping("/balance/{id}")
    public ResponseEntity<AccountDTO> updateBalance(@PathVariable("id") String id, @RequestBody BigDecimal newBalance) {
          Account updatedAccount = accountService.updateBalance(id, newBalance);
        return ResponseEntity.ok(AccountMapper.toDTO(updatedAccount));
    }


}

