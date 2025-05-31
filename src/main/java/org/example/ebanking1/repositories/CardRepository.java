package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    List<Card> findByAccount(Account account);
    List<Card> findByAccountAndStatus(Account account, String status);
    List<Card> findByType(String type);
    List<Card> findByNetwork(String network);
    List<Card> findByCardholderName(String name);
    List<Card> findByAccount_Id(String accountId);
}