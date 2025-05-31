package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.RechargeProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeProviderRepository extends JpaRepository<RechargeProvider, String> {
    List<RechargeProvider> findByIsActiveTrue();
    List<RechargeProvider> findByServiceType(String serviceType);
}