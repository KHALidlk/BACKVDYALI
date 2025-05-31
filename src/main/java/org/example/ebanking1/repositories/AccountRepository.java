package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Client;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByAccountNumber(String accountNumber);
    List<Account> findByClient(Client client);
    List<Account> findByClientAndStatus(Client client, String status);
    List<Account> findByType(String type);
    List<Account> findByOpenedDateAfter(LocalDateTime date);
    List<Account> findByIsPrimaryTrue();
    List<Account> findByCurrency(String currency);
}

