package org.example.ebanking1.service;

import org.example.ebanking1.entities.AlertSetting;
import org.example.ebanking1.repositories.AlertSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertService {

    private final AlertSettingRepository repository;

    @Autowired
    public AlertService(AlertSettingRepository repository) {
        this.repository = repository;
    }

    public AlertSetting save(AlertSetting entity) {
        return repository.save(entity);
    }

    public Optional<AlertSetting> findById(String id) {
        return repository.findById(id);
    }

    public List<AlertSetting> findAll() {
        return repository.findAll();
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Optional<AlertSetting> findByClientId(String clientId) {
        return repository.findByClient(clientId);
    }
}
