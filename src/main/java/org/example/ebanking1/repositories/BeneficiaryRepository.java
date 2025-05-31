package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Beneficiary;
import org.example.ebanking1.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, String> {
    List<Beneficiary> findByClient(Client client);
    List<Beneficiary> findByClientAndIsFavoriteTrue(Client client);
    List<Beneficiary> findByAccountNumber(String accountNumber);
    List<Beneficiary> findByBankCode(String bankCode);
    List<Beneficiary> findByType(String type);
}