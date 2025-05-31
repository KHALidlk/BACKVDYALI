package org.example.ebanking1.service;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Card;
import org.example.ebanking1.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository repository;

    @Autowired
    public CardService(CardRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Card save(Card entity) {
        return repository.save(entity);
    }

    public Optional<Card> findById(String id) {
        return repository.findById(id);
    }

    public List<Card> findAll() {
        return repository.findAll();
    }
    public List<Card> findByAccountId(String accountId) {
        return repository.findByAccount_Id(accountId);
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Card> findByAccount(Account account) {
        return repository.findByAccount(account);
    }

    public List<Card> findByAccountAndStatus(Account account, String status) {
        return repository.findByAccountAndStatus(account, status);
    }

    public List<Card> findByType(String type) {
        return repository.findByType(type);
    }

    public List<Card> findByNetwork(String network) {
        return repository.findByNetwork(network);
    }

    public List<Card> findByCardholderName(String name) {
        return repository.findByCardholderName(name);
    }

    @Transactional
    public Card activateCard(String id) {
        Card card = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
        card.setStatus("active");
        return repository.save(card);
    }

    @Transactional
    public Card blockCard(String id, String reason) {
        Card card = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + id));
        card.setStatus("blocked");
        card.setBlockReason(reason);
        return repository.save(card);
    }



}
