package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.Transfermodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransferRepository extends JpaRepository<Transfermodel, String> {
    List<Transfermodel> findBySenderAccount(Account senderAccount);
    List<Transfermodel> findByRecipientAccount(Account recipientAccount);
    List<Transfermodel> findBySenderAccountOrRecipientAccount(Account senderAccount, Account recipientAccount);
    List<Transfermodel> findByStatus(String status);

    // Find all completed transfers
    List<Transfermodel> findByStatusOrderByTransactionDateDesc(String status);

    // Find all completed transfers for a specific account (both sent and received)
    List<Transfermodel> findBySenderAccountOrRecipientAccountAndStatusOrderByTransactionDateDesc(
            Account senderAccount, Account recipientAccount, String status);

    // Find all completed outgoing transfers for an account
    List<Transfermodel> findBySenderAccountAndStatusOrderByTransactionDateDesc(Account senderAccount, String status);

    // Find all completed incoming transfers for an account
    List<Transfermodel> findByRecipientAccountAndStatusOrderByTransactionDateDesc(Account recipientAccount, String status);

    // User-based queries using JPQL - fixed to use StripeAccount's localAccount relationship
    @Query("SELECT t FROM Transfermodel t WHERE t.senderAccount.localAccount.id = :userId")
    List<Transfermodel> findBySenderUserId(@Param("userId") String userId);

    @Query("SELECT t FROM Transfermodel t WHERE t.recipientAccount.localAccount.id = :userId")
    List<Transfermodel> findByRecipientUserId(@Param("userId") String userId);

    @Query("SELECT t FROM Transfermodel t WHERE t.senderAccount.localAccount.id = :userId OR t.recipientAccount.localAccount.id = :userId")
    List<Transfermodel> findByUserIdInvolved(@Param("userId") String userId);

    @Query("SELECT t FROM Transfermodel t WHERE (t.senderAccount.localAccount.id = :userId OR t.recipientAccount.localAccount.id = :userId) AND t.status = :status ORDER BY t.transactionDate DESC")
    List<Transfermodel> findByUserIdInvolvedAndStatus(@Param("userId") String userId, @Param("status") String status);

    @Query("SELECT t FROM Transfermodel t WHERE t.senderAccount.localAccount.id = :userId AND t.status = :status ORDER BY t.transactionDate DESC")
    List<Transfermodel> findBySenderUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);

    @Query("SELECT t FROM Transfermodel t WHERE t.recipientAccount.localAccount.id = :userId AND t.status = :status ORDER BY t.transactionDate DESC")
    List<Transfermodel> findByRecipientUserIdAndStatus(@Param("userId") String userId, @Param("status") String status);

    List<Transfermodel> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
    List<Transfermodel> findByReference(String reference);
}
