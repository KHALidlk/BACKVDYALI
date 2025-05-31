package org.example.ebanking1.dto;

import org.example.ebanking1.entities.Client;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to convert between Client entities and DTOs
 */
public class ClientMapper {

    /**
     * Convert Client entity to ClientDto
     * @param client The client entity to convert
     * @return ClientDto representing the client
     */
    public static ClientDto toDTO(Client client) {
        if (client == null) {
            return null;
        }

        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setClientId(client.getClientId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setPhone(client.getPhone());
        dto.setIdentityNumber(client.getIdentityNumber());
        dto.setAddress(client.getAddress());
        dto.setCity(client.getCity());
        dto.setPostalCode(client.getPostalCode());
        dto.setCountry(client.getCountry());
        dto.setStatus(client.getStatus());
        dto.setAccountType(client.getAccountType());
        dto.setImageUrl(client.getImageUrl());
        dto.setDateJoined(client.getDateJoined());
        dto.setVerificationDate(client.getVerificationDate());
        dto.setCreatedAt(client.getCreatedAt());
        dto.setUpdatedAt(client.getUpdatedAt());

        // Map user information if available
        if (client.getUser() != null) {
            dto.setUserId(client.getUser().getId());
            dto.setUsername(client.getUser().getFullName());
            dto.setEmail(client.getUser().getEmail());
        }

        // Handle relationships safely to avoid LazyInitializationException
        if (client.getAccounts() != null) {
            dto.setNumberOfAccounts(client.getAccounts().size());
            dto.setAccounts(AccountMapper.toDTOList(client.getAccounts()));
        } else {
            dto.setNumberOfAccounts(0);
            dto.setAccounts(Collections.emptyList());
        }

        if (client.getBeneficiaries() != null) {
            dto.setNumberOfBeneficiaries(client.getBeneficiaries().size());
        } else {
            dto.setNumberOfBeneficiaries(0);
        }

        return dto;
    }

    /**
     * Convert a list of Client entities to a list of ClientDtos
     * @param clients The list of client entities to convert
     * @return List of ClientDto objects
     */
    public static List<ClientDto> toDTOList(List<Client> clients) {
        if (clients == null) {
            return Collections.emptyList();
        }

        return clients.stream()
                .map(ClientMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing Client entity from a ClientDto
     * This doesn't set all fields, only those that are editable by users
     * @param client The client entity to update
     * @param dto The DTO containing the new values
     * @return The updated client entity (not yet persisted)
     */
    public static Client updateEntityFromDto(Client client, ClientDto dto) {
        if (client == null || dto == null) {
            return client;
        }

        // Only update fields that should be editable
        if (dto.getFirstName() != null) client.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) client.setLastName(dto.getLastName());
        if (dto.getPhone() != null) client.setPhone(dto.getPhone());
        if (dto.getIdentityNumber() != null) client.setIdentityNumber(dto.getIdentityNumber());
        if (dto.getAddress() != null) client.setAddress(dto.getAddress());
        if (dto.getCity() != null) client.setCity(dto.getCity());
        if (dto.getPostalCode() != null) client.setPostalCode(dto.getPostalCode());
        if (dto.getCountry() != null) client.setCountry(dto.getCountry());
        if (dto.getImageUrl() != null) client.setImageUrl(dto.getImageUrl());

        return client;
    }
}
