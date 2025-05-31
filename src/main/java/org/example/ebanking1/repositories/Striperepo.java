package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.StripeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Striperepo extends JpaRepository<StripeAccount, String> {
    @Query("SELECT s FROM StripeAccount s WHERE s.localAccount.id = :localAccountId")
    List<StripeAccount> findByLocalAccountId(@Param("localAccountId") String localAccountId);
}
