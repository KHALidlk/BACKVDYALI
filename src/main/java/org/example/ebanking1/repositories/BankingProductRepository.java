package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.BankingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankingProductRepository extends JpaRepository<BankingProduct, String> {
    List<BankingProduct> findByIsActiveTrue();
    List<BankingProduct> findByType(String type);
    List<BankingProduct> findByCategory(String category);
}