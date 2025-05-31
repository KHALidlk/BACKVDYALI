package org.example.ebanking1.service;

import org.example.ebanking1.entities.BankingProduct;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.entities.ProductApplication;
import org.example.ebanking1.repositories.ProductApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductApplicationService {

    private final ProductApplicationRepository repository;

    @Autowired
    public ProductApplicationService(ProductApplicationRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProductApplication save(ProductApplication entity) {
        return repository.save(entity);
    }

    public Optional<ProductApplication> findById(String id) {
        return repository.findById(id);
    }

    public List<ProductApplication> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<ProductApplication> findByClient(Client client) {
        return repository.findByClient(client);
    }

    public List<ProductApplication> findByProduct(BankingProduct product) {
        return repository.findByProduct(product);
    }

    public List<ProductApplication> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<ProductApplication> findBySubmittedDateBetween(LocalDateTime start, LocalDateTime end) {
        return repository.findBySubmittedDateBetween(start, end);
    }

    public List<ProductApplication> findApplicationsSubmittedThisMonth() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime firstDayOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return repository.findBySubmittedDateBetween(firstDayOfMonth, now);
    }
    
    @Transactional
    public void updateApplicationStatus(String id, String status) {
        Optional<ProductApplication> applicationOpt = repository.findById(id);
        if (applicationOpt.isPresent()) {
            ProductApplication application = applicationOpt.get();
            application.setStatus(status);
            application.setUpdatedAt(LocalDateTime.now());
            repository.save(application);
        } else {
            throw new IllegalArgumentException("Product application not found with id: " + id);
        }
    }
}
