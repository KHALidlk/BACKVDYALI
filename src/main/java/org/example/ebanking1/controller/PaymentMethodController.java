package org.example.ebanking1.controller;

import org.example.ebanking1.entities.PaymentMethod;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payment-methods")  // ✅ L’URL de base ➔ http://localhost:8080/payment-methods
public class PaymentMethodController {

    private final PaymentMethodService service;

    @Autowired
    public PaymentMethodController(PaymentMethodService service) {
        this.service = service;
    }

    // ✅ 1) Ajouter une méthode de paiement
    @PostMapping("/add")
    public PaymentMethod add(@RequestBody PaymentMethod method) {
        return service.save(method);
    }

    // ✅ 2) Lister toutes les méthodes enregistrées (tous utilisateurs)
    @GetMapping
    public List<PaymentMethod> getAll() {
        return service.findAll();
    }

    // ✅ 3) Récupérer une méthode par ID
    @GetMapping("/{id}")
    public Optional<PaymentMethod> getById(@PathVariable String id) {
        return service.findById(id);
    }
    // ✅ 4) Supprimer une méthode par ID
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }
    // ✅ 5) Lister les méthodes d’un utilisateur donné
    @GetMapping("/user/{userId}")
    public List<PaymentMethod> getByUser(@PathVariable User user) {
        return service.findByUser(user);
    }
}
