package org.example.ebanking1.service;

import jakarta.transaction.Transactional;
import org.example.ebanking1.dto.TransferRequestDTO;
import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.StripeAccount;
import org.example.ebanking1.entities.Transfermodel;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.AccountRepository;
import org.example.ebanking1.repositories.Striperepo;
import org.example.ebanking1.repositories.TransferRepository;
import org.example.ebanking1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Striperepo stripeRepository;

    @Autowired
    public TransferService(TransferRepository transferRepository,
                          AccountRepository accountRepository,
                          UserRepository userRepository,
                          Striperepo stripeRepository) {
        this.transferRepository = transferRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.stripeRepository = stripeRepository;
    }

    /**
     * Process a transfer between two accounts
     */
    @Transactional
    public Transfermodel processTransfer(TransferRequestDTO transferRequest) throws Exception {
        // Validate the request
        if (transferRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        // Validate that users exist
        Optional<User> sourceUserOpt = userRepository.findById(transferRequest.getSourceUserId());
        Optional<User> destinationUserOpt = userRepository.findById(transferRequest.getDestinationUserId());

        if (sourceUserOpt.isEmpty()) {
            throw new Exception("Source user not found");
        }

        if (destinationUserOpt.isEmpty()) {
            throw new Exception("Destination user not found");
        }

        User sourceUser = sourceUserOpt.get();
        User destinationUser = destinationUserOpt.get();

        // Fetch accounts
        Optional<Account> sourceAccountOpt = accountRepository.findById(transferRequest.getSourceAccountId());
        Optional<Account> destinationAccountOpt = accountRepository.findById(transferRequest.getDestinationAccountId());

        if (sourceAccountOpt.isEmpty()) {
            throw new Exception("Source account not found");
        }

        if (destinationAccountOpt.isEmpty()) {
            throw new Exception("Destination account not found");
        }

        Account sourceAccount = sourceAccountOpt.get();
        Account destinationAccount = destinationAccountOpt.get();

        // Verify that the source account belongs to the source user
        if (sourceAccount.getClient() == null ||
            sourceAccount.getClient().getUser() == null ||
            !sourceAccount.getClient().getUser().getId().equals(sourceUser.getId())) {
            throw new Exception("Source account does not belong to the specified user");
        }

        // Verify that the destination account belongs to the destination user
        if (destinationAccount.getClient() == null ||
            destinationAccount.getClient().getUser() == null ||
            !destinationAccount.getClient().getUser().getId().equals(destinationUser.getId())) {
            throw new Exception("Destination account does not belong to the specified user");
        }

        // Check if accounts are active
        if (!"active".equalsIgnoreCase(sourceAccount.getStatus())) {
            throw new Exception("Source account is not active");
        }

        if (!"active".equalsIgnoreCase(destinationAccount.getStatus())) {
            throw new Exception("Destination account is not active");
        }

        // Check sufficient funds
        if (sourceAccount.getAvailableBalance().compareTo(transferRequest.getAmount()) < 0) {
            throw new Exception("Insufficient funds in source account");
        }

        // Check currency match or handle conversion
        if (!transferRequest.getCurrency().equalsIgnoreCase(sourceAccount.getCurrency()) ||
            !transferRequest.getCurrency().equalsIgnoreCase(destinationAccount.getCurrency())) {
            throw new Exception("Currency mismatch. Both accounts must use the same currency as the transfer");
        }

        // Get StripeAccount entities for sender and recipient
        List<StripeAccount> senderStripeAccounts = stripeRepository.findByLocalAccountId(transferRequest.getSourceUserId());
        List<StripeAccount> recipientStripeAccounts = stripeRepository.findByLocalAccountId(transferRequest.getDestinationUserId());

        if (senderStripeAccounts.isEmpty()) {
            throw new Exception("Source user does not have a Stripe account");
        }

        if (recipientStripeAccounts.isEmpty()) {
            throw new Exception("Destination user does not have a Stripe account");
        }

        StripeAccount senderStripeAccount = senderStripeAccounts.get(0);
        StripeAccount recipientStripeAccount = recipientStripeAccounts.get(0);

        // Create transfer record
        Transfermodel transfermodel = new Transfermodel();
        transfermodel.setSenderAccount(senderStripeAccount);
        transfermodel.setRecipientAccount(recipientStripeAccount);
        transfermodel.setAmount(transferRequest.getAmount());
        transfermodel.setCurrency(transferRequest.getCurrency());
        transfermodel.setDescription(transferRequest.getDescription());
        transfermodel.setReference(transferRequest.getReference() != null ?
                transferRequest.getReference() : generateReference());
        transfermodel.setStatus("pending");
        transfermodel.setTransactionDate(LocalDateTime.now());
        transfermodel.setCreatedAt(LocalDateTime.now());
        transfermodel.setUpdatedAt(LocalDateTime.now());

        // Save initial transfer record
        transfermodel = transferRepository.save(transfermodel);

        try {
            // Update balances
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequest.getAmount()));
            sourceAccount.setAvailableBalance(sourceAccount.getAvailableBalance().subtract(transferRequest.getAmount()));
            sourceAccount.setLastTransactionDate(LocalDateTime.now());
            sourceAccount.setUpdatedAt(LocalDateTime.now());

            destinationAccount.setBalance(destinationAccount.getBalance().add(transferRequest.getAmount()));
            destinationAccount.setAvailableBalance(destinationAccount.getAvailableBalance().add(transferRequest.getAmount()));
            destinationAccount.setLastTransactionDate(LocalDateTime.now());
            destinationAccount.setUpdatedAt(LocalDateTime.now());

            // Save updated accounts
            accountRepository.save(sourceAccount);
            accountRepository.save(destinationAccount);

            // Update transfer status
            transfermodel.setStatus("completed");
            transfermodel.setProcessingDate(LocalDateTime.now());
            transfermodel.setUpdatedAt(LocalDateTime.now());
            transfermodel = transferRepository.save(transfermodel);

            return transfermodel;
        } catch (Exception e) {
            // If anything goes wrong, mark transfer as failed
            transfermodel.setStatus("failed");
            transfermodel.setUpdatedAt(LocalDateTime.now());
            transferRepository.save(transfermodel);

            throw new Exception("Transfer processing failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get all transfers for an account (both sent and received)
     */
    public List<Transfermodel> getAccountTransfers(String accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            return List.of(); // Return empty list if account not found
        }

        Account account = accountOpt.get();
        return transferRepository.findBySenderAccountOrRecipientAccount(account, account);
    }

    /**
     * Get outgoing transfers from an account
     */
    public List<Transfermodel> getOutgoingTransfers(String accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            return List.of(); // Return empty list if account not found
        }

        Account account = accountOpt.get();
        return transferRepository.findBySenderAccount(account);
    }

    /**
     * Get incoming transfers to an account
     */
    public List<Transfermodel> getIncomingTransfers(String accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            return List.of(); // Return empty list if account not found
        }

        Account account = accountOpt.get();
        return transferRepository.findByRecipientAccount(account);
    }

    /**
     * Get all completed transfers
     */
    public List<Transfermodel> getAllCompletedTransfers() {
        return transferRepository.findByStatusOrderByTransactionDateDesc("completed");
    }

    /**
     * Get all completed transfers for a specific account (both sent and received)
     */
    public List<Transfermodel> getCompletedTransfersForAccount(String accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            return List.of(); // Return empty list if account not found
        }

        Account account = accountOpt.get();
        return transferRepository.findBySenderAccountOrRecipientAccountAndStatusOrderByTransactionDateDesc(
                account, account, "completed");
    }

    /**
     * Get all completed outgoing transfers for an account
     */
    public List<Transfermodel> getCompletedOutgoingTransfers(String accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            return List.of(); // Return empty list if account not found
        }

        Account account = accountOpt.get();
        return transferRepository.findBySenderAccountAndStatusOrderByTransactionDateDesc(account, "completed");
    }

    /**
     * Get all completed incoming transfers for an account
     */
    public List<Transfermodel> getCompletedIncomingTransfers(String accountId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        if (accountOpt.isEmpty()) {
            return List.of(); // Return empty list if account not found
        }

        Account account = accountOpt.get();
        return transferRepository.findByRecipientAccountAndStatusOrderByTransactionDateDesc(account, "completed");
    }

    /**
     * Get all transfers for a user (both sent and received)
     */
    public List<Transfermodel> getUserTransfers(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of(); // Return empty list if user not found
        }

        return transferRepository.findByUserIdInvolved(userId);
    }
   public List<Transfermodel> getAll() {
        return transferRepository.findAll();
   }


        /**
         * Get outgoing transfers from a user
         */
    public List<Transfermodel> getUserOutgoingTransfers(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of(); // Return empty list if user not found
        }

        return transferRepository.findBySenderUserId(userId);
    }

    /**
     * Get incoming transfers to a user
     */
    public List<Transfermodel> getUserIncomingTransfers(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of(); // Return empty list if user not found
        }

        return transferRepository.findByRecipientUserId(userId);
    }

    /**
     * Get all completed transfers for a specific user (both sent and received)
     */
    public List<Transfermodel> getCompletedTransfersForUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of(); // Return empty list if user not found
        }

        return transferRepository.findByUserIdInvolvedAndStatus(userId, "completed");
    }

    /**
     * Get all completed outgoing transfers for a user
     */
    public List<Transfermodel> getCompletedOutgoingTransfersForUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of(); // Return empty list if user not found
        }

        return transferRepository.findBySenderUserIdAndStatus(userId, "completed");
    }

    /**
     * Get all completed incoming transfers for a user
     */
    public List<Transfermodel> getCompletedIncomingTransfersForUser(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return List.of(); // Return empty list if user not found
        }

        return transferRepository.findByRecipientUserIdAndStatus(userId, "completed");
    }

    /**
     * Get a specific transfer by ID
     */
    public Optional<Transfermodel> getTransferById(String transferId) {
        return transferRepository.findById(transferId);
    }

    /**
     * Generate a unique reference for the transfer
     */
    private String generateReference() {
        return "TRF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
