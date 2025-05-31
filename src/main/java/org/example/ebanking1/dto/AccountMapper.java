package org.example.ebanking1.dto;

import org.example.ebanking1.entities.Account;
import org.example.ebanking1.entities.AccountBankNumber;
import org.example.ebanking1.entities.Client;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to convert between Account entities and DTOs
 */
public class AccountMapper {

    /**
     * Convert Account entity to AccountDTO
     */
    public static AccountDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountDTO dto = new AccountDTO();
        dto.setId(account.getId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        dto.setAvailableBalance(account.getAvailableBalance());
        dto.setCurrency(account.getCurrency());
        dto.setStatus(account.getStatus());
        dto.setOpenedDate(account.getOpenedDate());
        dto.setLastTransactionDate(account.getLastTransactionDate());
        dto.setIban(account.getIban());
        dto.setDailyLimit(account.getDailyLimit());
        dto.setMonthlyLimit(account.getMonthlyLimit());
        dto.setIsPrimary(account.getIsPrimary());
        dto.setInterestRate(account.getInterestRate());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());

        // Map client information if available
        Client client = account.getClient();
        if (client != null) {
            dto.setClientId(client.getId());
            dto.setClientFirstName(client.getFirstName());
            dto.setClientLastName(client.getLastName());
        }

        // Map account bank number information if available
        AccountBankNumber bankNumber = account.getAccountBankNumber();
        if (bankNumber != null) {
            dto.setBankNumberId(bankNumber.getId());
            dto.setBankCode(bankNumber.getBankCode());
            dto.setCityCode(bankNumber.getCityCode());
            dto.setCountryCode(bankNumber.getCountryCode());
            dto.setRibKey(bankNumber.getRibKey());
        }

        return dto;
    }

    /**
     * Convert a list of Account entities to a list of AccountDTOs
     */
    public static List<AccountDTO> toDTOList(List<Account> accounts) {
        return accounts.stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }
}
