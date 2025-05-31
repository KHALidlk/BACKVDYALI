package org.example.ebanking1.service;

import org.example.ebanking1.entities.Client;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.ClientRepository;
import org.example.ebanking1.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, UserRepository userRepository) {
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public List<Client> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        // Initialize collections for each client to prevent LazyInitializationException
        clients.forEach(this::initializeClientCollections);
        return clients;
    }

    public Optional<Client> getClientById(String id) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        clientOpt.ifPresent(this::initializeClientCollections);
        return clientOpt;
    }

    public Optional<Client> getClientByClientId(String clientId) {
        Optional<Client> clientOpt = clientRepository.findByClientId(clientId);
        clientOpt.ifPresent(this::initializeClientCollections);
        return clientOpt;
    }

    public List<Client> getClientsByStatus(String status) {
        List<Client> clients = clientRepository.findByStatus(status);
        clients.forEach(this::initializeClientCollections);
        return clients;
    }

    @Transactional
    public Client createClient(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Client client = new Client();
        // Generate a unique client ID (you might want to customize this)
        String clientId = "CL" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        client.setClientId(clientId);

        client.setId(UUID.randomUUID().toString());
        client.setUser(user);
        client.setStatus("active");
        client.setDateJoined(LocalDateTime.now());
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        return clientRepository.save(client);
    }

    @Transactional
    public Client updateClient(String id, Client clientDetails) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        if (clientDetails.getFirstName() != null) client.setFirstName(clientDetails.getFirstName());
        if (clientDetails.getLastName() != null) client.setLastName(clientDetails.getLastName());
        if (clientDetails.getPhone() != null) client.setPhone(clientDetails.getPhone());
        if (clientDetails.getAddress() != null) client.setAddress(clientDetails.getAddress());
        if (clientDetails.getCity() != null) client.setCity(clientDetails.getCity());
        if (clientDetails.getPostalCode() != null) client.setPostalCode(clientDetails.getPostalCode());
        if (clientDetails.getCountry() != null) client.setCountry(clientDetails.getCountry());
        if (clientDetails.getStatus() != null) client.setStatus(clientDetails.getStatus());
        if (clientDetails.getAccountType() != null) client.setAccountType(clientDetails.getAccountType());
        if (clientDetails.getImageUrl() != null) client.setImageUrl(clientDetails.getImageUrl());

        client.setUpdatedAt(LocalDateTime.now());
        Client updatedClient = clientRepository.save(client);
        initializeClientCollections(updatedClient);
        return updatedClient;
    }

    @Transactional
    public void deleteClient(String id) {
        // Consider soft delete or check for accounts before hard delete
        clientRepository.deleteById(id);
    }

    /**
     * Find client by user ID
     * @param userId the user ID to search for
     * @return optional containing the client if found
     */
    public Optional<Client> findByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Optional<Client> clientOpt = clientRepository.findAll().stream()
                .filter(client -> client.getUser() != null && client.getUser().getId().equals(userId))
                .findFirst();

        clientOpt.ifPresent(this::initializeClientCollections);
        return clientOpt;
    }

    @Transactional
    public void verifyClient(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setVerificationDate(LocalDateTime.now());
        client.setStatus("verified");
        client.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(client);
    }

    @Transactional
    public void updateClientStatus(String id, String status) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        client.setStatus(status);
        client.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(client);
    }

    /**
     * Initialize all collections in Client entity to prevent LazyInitializationException
     * This forces Hibernate to load the collections while the session is still open
     * @param client the client entity to initialize collections for
     */
    private void initializeClientCollections(Client client) {
        if (client == null) return;

        // Force initialization of collections
        Hibernate.initialize(client.getAccounts());
        Hibernate.initialize(client.getBeneficiaries());
        Hibernate.initialize(client.getAlertSettings());
        Hibernate.initialize(client.getProductApplications());
    }
}
