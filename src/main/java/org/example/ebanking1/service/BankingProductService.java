package org.example.ebanking1.service;

import org.example.ebanking1.entities.BankingProduct;
import org.example.ebanking1.repositories.BankingProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BankingProductService {

    private final BankingProductRepository repository;

    @Autowired
    public BankingProductService(BankingProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public BankingProduct save(BankingProduct entity) {
        return repository.save(entity);
    }

    public Optional<BankingProduct> findById(String id) {
        return repository.findById(id);
    }

    public List<BankingProduct> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<BankingProduct> findActiveProducts() {
        return repository.findByIsActiveTrue();
    }

    public List<BankingProduct> findByType(String type) {
        return repository.findByType(type);
    }

    public List<BankingProduct> findByCategory(String category) {
        return repository.findByCategory(category);
    }

    @Transactional
    public BankingProduct activateProduct(String id) {
        BankingProduct product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banking product not found with id: " + id));
        product.setIsActive(true);
        return repository.save(product);
    }

    @Transactional
    public BankingProduct deactivateProduct(String id) {
        BankingProduct product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Banking product not found with id: " + id));
        product.setIsActive(false);
        return repository.save(product);
    }
}
