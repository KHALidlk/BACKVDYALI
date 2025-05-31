package org.example.ebanking1.controller;
import org.example.ebanking1.entities.AlertSetting;
import org.example.ebanking1.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController@RequestMapping("/alerts")
public class AlertController {

    private final AlertService service;

    @Autowired
    public AlertController(AlertService service) {
        this.service = service;
    }

    @PostMapping
    public AlertSetting save(@RequestBody AlertSetting entity) {
        return service.save(entity);
    }

    @GetMapping("/{id}")
    public Optional<AlertSetting> findById(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping
    public List<AlertSetting> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/client/{clientId}")
    public Optional<AlertSetting> findByClient(@PathVariable String clientId) {
        return service.findByClientId(clientId);
    }
}
