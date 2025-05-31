package org.example.ebanking1.service;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.entities.Document;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DocumentService {

    private final DocumentRepository repository;
    private final AccountService accountService;

    @Autowired
    public DocumentService(DocumentRepository repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
    }

    @Transactional
    public Document save(Document entity) {
        return repository.save(entity);
    }

    public Optional<Document> findById(String id) {
        return repository.findById(id);
    }

    public List<Document> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Document> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<Document> findByAccount(Account account) {
        return repository.findByAccount(account);
    }

    public List<Document> findByType(String type) {
        return repository.findByType(type);
    }

    public List<Document> findUnreadDocuments() {
        return repository.findByIsReadFalse();
    }

    public List<Document> findArchivedDocuments() {
        return repository.findByIsArchivedTrue();
    }

    public List<Document> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findByCreatedAtBetween(start, end);
    }

    @Transactional
    public Document markAsRead(String id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        document.setIsRead(true);
        document.setUpdatedAt(LocalDateTime.now());
        return repository.save(document);
    }

    @Transactional
    public Document archiveDocument(String id) {
        Document document = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));
        document.setIsArchived(true);
        document.setArchivedDate(LocalDateTime.now());
        return repository.save(document);
    }
    @Transactional
    public Document relverBancair(String accountId,String clientid) {
        Document document = new Document();
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
        Client client= account.getClient();
       User user= account.getClient().getUser();
        document.setAccount(account);
        document.setUser(user);
        document.setType("statement");
        document.setTitle("Bank Statement");
        document.setDescription("Bank statement for account " + account.getAccountNumber());
        document.setIsRead(false);
        document.setIsArchived(false);
        document.setCreatedAt(LocalDateTime.now());
        document.setPeriodStart(LocalDateTime.now().minusMonths(1));
        document.setPeriodEnd(LocalDateTime.now());
        String message = String.format(
                "Dear %s, your bank statement for account %s covers the period from %s to %s. Current balance: %.2f MAD.",
                client.getUser().getFullName(), // suppose que tu as un getFullName()
                account.getAccountNumber(),
                LocalDateTime.now().minusMonths(1).toLocalDate(),
                LocalDateTime.now().toLocalDate(),
                account.getBalance()
        );
        document.setMessage(message);
        return repository.save(document);
    }
}
