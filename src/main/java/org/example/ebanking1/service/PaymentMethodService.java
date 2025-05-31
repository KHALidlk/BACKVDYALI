package org.example.ebanking1.service;
import org.example.ebanking1.entities.PaymentMethod;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PaymentMethodService {

    private final PaymentMethodRepository repository;

    @Autowired
    public PaymentMethodService(PaymentMethodRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public PaymentMethod save(PaymentMethod entity) {
        return repository.save(entity);
    }

    public Optional<PaymentMethod> findById(String id) {
        return repository.findById(id);
    }

    public List<PaymentMethod> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public List<PaymentMethod> findByUser(User user) {
        return repository.findByUser(user);
    }

    public List<PaymentMethod> findActiveByUser(User user) {
        return repository.findByUserAndIsActiveTrue(user);
    }

    public List<PaymentMethod> findDefaultByUser(User user) {
        return repository.findByUserAndIsDefaultTrue(user);
    }

    public List<PaymentMethod> findByType(String type) {
        return repository.findByType(type);
    }

    @Transactional
    public PaymentMethod setAsDefault(String id, User user) {
        // First, unset default for all user's payment methods
        List<PaymentMethod> userDefaultMethods = repository.findByUserAndIsDefaultTrue(user);
        for (PaymentMethod method : userDefaultMethods) {
            method.setIsDefault(false);
            repository.save(method);
        }

        // Then set the specified one as default
        PaymentMethod paymentMethod = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));

        // Ensure the payment method belongs to the user
        if (!paymentMethod.getUser().equals(user)) {
            throw new RuntimeException("Payment method does not belong to the specified user");
        }

        paymentMethod.setIsDefault(true);
        return repository.save(paymentMethod);
    }

    @Transactional
    public PaymentMethod activate(String id) {
        PaymentMethod paymentMethod = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));
        paymentMethod.setIsActive(true);
        return repository.save(paymentMethod);
    }

    @Transactional
    public PaymentMethod deactivate(String id) {
        PaymentMethod paymentMethod = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found with id: " + id));
        paymentMethod.setIsActive(false);
        return repository.save(paymentMethod);
    }
}
