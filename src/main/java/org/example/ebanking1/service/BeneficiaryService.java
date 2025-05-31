package org.example.ebanking1.service;

import org.example.ebanking1.entities.Beneficiary;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.repositories.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BeneficiaryService {

    private final BeneficiaryRepository repository;

    @Autowired
    public BeneficiaryService(BeneficiaryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Beneficiary save(Beneficiary entity) {
        return repository.save(entity);
    }

    public Optional<Beneficiary> findById(String id) {
        return repository.findById(id);
    }

    public List<Beneficiary> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<Beneficiary> findByClient(Client client) {
        return repository.findByClient(client);
    }

    public List<Beneficiary> findFavoritesByClient(Client client) {
        return repository.findByClientAndIsFavoriteTrue(client);
    }

    public List<Beneficiary> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber);
    }

    public List<Beneficiary> findByBankCode(String bankCode) {
        return repository.findByBankCode(bankCode);
    }

    public List<Beneficiary> findByType(String type) {
        return repository.findByType(type);
    }

    @Transactional
    public Beneficiary markAsFavorite(String id) {
        Beneficiary beneficiary = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));
        beneficiary.setIsFavorite(true);
        return repository.save(beneficiary);
    }

    @Transactional
    public Beneficiary unmarkAsFavorite(String id) {
        Beneficiary beneficiary = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Beneficiary not found with id: " + id));
        beneficiary.setIsFavorite(false);
        return repository.save(beneficiary);
    }
}
