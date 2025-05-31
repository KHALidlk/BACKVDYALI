package org.example.ebanking1.service;

import org.example.ebanking1.dto.AccountDTO;
import org.example.ebanking1.dto.AccountMapper;
import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.AccountBankNumber;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.repositories.AccountBankNumberRepository;
import org.example.ebanking1.repositories.AccountRepository;
import org.example.ebanking1.repositories.ClientRepository;
import org.example.ebanking1.service.AccountBankNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final AccountBankNumberRepository accountBankNumberRepository;

    private final AccountBankNumberService accountBankNumberService;

    @Autowired
    public AccountService(
            AccountRepository accountRepository,
            ClientRepository clientRepository,

            AccountBankNumberRepository accountBankNumberRepository,
            AccountBankNumberService accountBankNumberService) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.accountBankNumberRepository = accountBankNumberRepository;
        this.accountBankNumberService = accountBankNumberService;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepository.findById(id);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> getAccountsByClient(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        return accountRepository.findByClient(client);
    }

    public List<Account> getActiveAccountsByClient(String clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));
        return accountRepository.findByClientAndStatus(client, "active");
    }
    @Transactional
    public Account createAccount(String clientId, String currencyId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        // Validate currency code - only accept valid, alphanumeric currency codes
        String currencyCode = "EUR"; // Default to EUR for simplicity


        // Generate account number
        String accountNumber = generateAccountNumber();
        Account account = new Account();
        account.setId(UUID.randomUUID().toString());
        account.setClient(client);
        account.setCurrency(currencyCode);
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);
        account.setAvailableBalance(BigDecimal.ZERO);
        account.setStatus("active");
        account.setOpenedDate(LocalDateTime.now());
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());

        // Save the account
        Account savedAccount = accountRepository.save(account);

        // Create and link account bank number
        createAccountBankNumber(savedAccount);

        return savedAccount;
    }

    private void createAccountBankNumber(Account account) {
        // Use the dedicated service to create an account bank number
        AccountBankNumber bankNumber = accountBankNumberService.createAccountBankNumber(account);
        // Generate and set IBAN
        String iban = accountBankNumberService.generateIBAN(bankNumber);
        account.setIban(iban);
        accountRepository.save(account);
    }

    private String generateAccountNumber() {
        // Generate a random 10-digit account number
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    @Transactional
    public Account updateAccount(String id, Account accountDetails) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        if (accountDetails.getType() != null) account.setType(accountDetails.getType());
        if (accountDetails.getStatus() != null) account.setStatus(accountDetails.getStatus());
        if (accountDetails.getIban() != null) account.setIban(accountDetails.getIban());
        if (accountDetails.getDailyLimit() != null) account.setDailyLimit(accountDetails.getDailyLimit());
        if (accountDetails.getMonthlyLimit() != null) account.setMonthlyLimit(accountDetails.getMonthlyLimit());
        if (accountDetails.getIsPrimary() != null) account.setIsPrimary(accountDetails.getIsPrimary());
        if (accountDetails.getInterestRate() != null) account.setInterestRate(accountDetails.getInterestRate());

        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(String id) {
        // Consider deactivating instead of deleting
        accountRepository.deleteById(id);
    }

    @Transactional
    public void updateAccountStatus(String id, String status) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        account.setStatus(status);
        account.setUpdatedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Transactional
    public Account updateBalance(String id, BigDecimal newBalance) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));

        account.setBalance(newBalance);
        account.setAvailableBalance(newBalance);  // Simplified; in reality, available balance might differ
        account.setLastTransactionDate(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        Account savedAccount = accountRepository.save(account);
        return savedAccount;
    }
}

