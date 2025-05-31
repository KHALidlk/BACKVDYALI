package org.example.ebanking1.controller;

import org.example.ebanking1.dto.TransferRequestDTO;
import org.example.ebanking1.entities.Transfermodel;
import org.example.ebanking1.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    private final TransferService transferService;

    @Autowired
    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public ResponseEntity<?> createTransfer(@RequestBody TransferRequestDTO transferRequest, @RequestParam(required = false) String currentUserId) {
        try {
            // If currentUserId is provided in the request params, use it as the sourceUserId
            if (currentUserId != null && !currentUserId.isEmpty()) {
                transferRequest.setSourceUserId(currentUserId);
            }

            // Validate that sourceUserId is provided either in the request body or as a parameter
            if (transferRequest.getSourceUserId() == null || transferRequest.getSourceUserId().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Source user ID must be provided"));
            }

            Transfermodel transfermodel = transferService.processTransfer(transferRequest);
            return ResponseEntity.ok(transfermodel);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
 @GetMapping
    public ResponseEntity<List<Transfermodel>> getAllTransfers() {
        List<Transfermodel> transfermodels = transferService.getAll();
        return ResponseEntity.ok(transfermodels);
    }
    // Original account-based endpoints
    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transfermodel>> getAccountTransfers(@PathVariable String accountId) {
        List<Transfermodel> transfermodels = transferService.getAccountTransfers(accountId);
        return ResponseEntity.ok(transfermodels);
    }

    @GetMapping("/account/{accountId}/outgoing")
    public ResponseEntity<List<Transfermodel>> getOutgoingTransfers(@PathVariable String accountId) {
        List<Transfermodel> transfermodels = transferService.getOutgoingTransfers(accountId);
        return ResponseEntity.ok(transfermodels);
    }

    @GetMapping("/account/{accountId}/incoming")
    public ResponseEntity<List<Transfermodel>> getIncomingTransfers(@PathVariable String accountId) {
        List<Transfermodel> transfermodels = transferService.getIncomingTransfers(accountId);
        return ResponseEntity.ok(transfermodels);
    }

    @GetMapping("/{transferId}")
    public ResponseEntity<?> getTransferById(@PathVariable String transferId) {
        Optional<Transfermodel> transfer = transferService.getTransferById(transferId);
        if (transfer.isPresent()) {
            return ResponseEntity.ok(transfer.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // New endpoints that use user IDs instead of account IDs
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Transfermodel>> getUserTransfers(@PathVariable String userId) {
        List<Transfermodel> transfermodels = transferService.getUserTransfers(userId);
        return ResponseEntity.ok(transfermodels);
    }

    @GetMapping("/user/{userId}/outgoing")
    public ResponseEntity<List<Transfermodel>> getUserOutgoingTransfers(@PathVariable String userId) {
        List<Transfermodel> transfermodels = transferService.getUserOutgoingTransfers(userId);
        return ResponseEntity.ok(transfermodels);
    }

    @GetMapping("/user/{userId}/incoming")
    public ResponseEntity<List<Transfermodel>> getUserIncomingTransfers(@PathVariable String userId) {
        List<Transfermodel> transfermodels = transferService.getUserIncomingTransfers(userId);
        return ResponseEntity.ok(transfermodels);
    }

    // New endpoints for completed transfers (account-based)
    @GetMapping("/completed")
    public ResponseEntity<List<Transfermodel>> getAllCompletedTransfers() {
        List<Transfermodel> completedTransfermodels = transferService.getAllCompletedTransfers();
        return ResponseEntity.ok(completedTransfermodels);
    }

    @GetMapping("/account/{accountId}/completed")
    public ResponseEntity<List<Transfermodel>> getCompletedTransfersForAccount(@PathVariable String accountId) {
        List<Transfermodel> completedTransfermodels = transferService.getCompletedTransfersForAccount(accountId);
        return ResponseEntity.ok(completedTransfermodels);
    }

    @GetMapping("/account/{accountId}/outgoing/completed")
    public ResponseEntity<List<Transfermodel>> getCompletedOutgoingTransfers(@PathVariable String accountId) {
        List<Transfermodel> completedTransfermodels = transferService.getCompletedOutgoingTransfers(accountId);
        return ResponseEntity.ok(completedTransfermodels);
    }

    @GetMapping("/account/{accountId}/incoming/completed")
    public ResponseEntity<List<Transfermodel>> getCompletedIncomingTransfers(@PathVariable String accountId) {
        List<Transfermodel> completedTransfermodels = transferService.getCompletedIncomingTransfers(accountId);
        return ResponseEntity.ok(completedTransfermodels);
    }

    // New endpoints for completed transfers (user-based)
    @GetMapping("/user/{userId}/completed")
    public ResponseEntity<List<Transfermodel>> getCompletedTransfersForUser(@PathVariable String userId) {
        List<Transfermodel> completedTransfermodels = transferService.getCompletedTransfersForUser(userId);
        return ResponseEntity.ok(completedTransfermodels);
    }

    @GetMapping("/user/{userId}/outgoing/completed")
    public ResponseEntity<List<Transfermodel>> getCompletedOutgoingTransfersForUser(@PathVariable String userId) {
        List<Transfermodel> completedTransfermodels = transferService.getCompletedOutgoingTransfersForUser(userId);
        return ResponseEntity.ok(completedTransfermodels);
    }

    @GetMapping("/user/{userId}/incoming/completed")
    public ResponseEntity<List<Transfermodel>> getCompletedIncomingTransfersForUser(@PathVariable String userId) {
        List<Transfermodel> completedTransfermodels = transferService.getCompletedIncomingTransfersForUser(userId);
        return ResponseEntity.ok(completedTransfermodels);
    }
}
