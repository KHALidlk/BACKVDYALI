package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {
    Optional<Client> findByClientId(String clientId);
    List<Client> findByStatus(String status);
    List<Client> findByAccountType(String accountType);
}