package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.PaymentMethod;
import org.example.ebanking1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {
    List<PaymentMethod> findByUser(User user);
    List<PaymentMethod> findByUserAndIsActiveTrue(User user);
    List<PaymentMethod> findByUserAndIsDefaultTrue(User user);
    List<PaymentMethod> findByType(String type);
}