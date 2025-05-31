package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.BankingProduct;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.entities.ProductApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductApplicationRepository extends JpaRepository<ProductApplication, String> {
    List<ProductApplication> findByClient(Client client);
    List<ProductApplication> findByProduct(BankingProduct product);
    List<ProductApplication> findByStatus(String status);
    List<ProductApplication> findBySubmittedDateBetween(LocalDateTime start, LocalDateTime end);
}