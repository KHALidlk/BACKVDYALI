package org.example.ebanking1.service;

import org.example.ebanking1.entities.RechargeProvider;
import org.example.ebanking1.repositories.RechargeProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RechargeProviderService {

    private final RechargeProviderRepository repository;

    @Autowired
    public RechargeProviderService(RechargeProviderRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RechargeProvider save(RechargeProvider entity) {
        return repository.save(entity);
    }

    public Optional<RechargeProvider> findById(String id) {
        return repository.findById(id);
    }

    public List<RechargeProvider> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<RechargeProvider> findActiveProviders() {
        return repository.findByIsActiveTrue();
    }

    public List<RechargeProvider> findByServiceType(String serviceType) {
        return repository.findByServiceType(serviceType);
    }

    @Transactional
    public RechargeProvider activateProvider(String id) {
        RechargeProvider provider = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recharge provider not found with id: " + id));
        provider.setIsActive(true);
        return repository.save(provider);
    }

    @Transactional
    public RechargeProvider deactivateProvider(String id) {
        RechargeProvider provider = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recharge provider not found with id: " + id));
        provider.setIsActive(false);
        return repository.save(provider);
    }
}
