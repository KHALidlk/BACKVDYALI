package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.AccountBankNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountBankNumberRepository extends JpaRepository<AccountBankNumber, String> {
    /**
     * Find an account bank number by account
     * @param account the account to search for
     * @return the found entity or empty optional
     */
    Optional<AccountBankNumber> findByAccount(Account account);
    
    /**
     * Find all account bank numbers associated with the given account ID
     * @param accountId the account ID to search for
     * @return list of bank numbers
     */
    List<AccountBankNumber> findByAccountId(String accountId);
    

}

