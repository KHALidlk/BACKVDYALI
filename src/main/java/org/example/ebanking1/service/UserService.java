package org.example.ebanking1.service;

import com.stripe.exception.StripeException;
import org.example.ebanking1.entities.*;
import org.example.ebanking1.repositories.ClientRepository;
import org.example.ebanking1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository repository;
    private final ClientRepository clientRepository;
    private final AccountService accountService;
    private final CardService cardService;
    private final AgentService agentService;

    @Autowired
    public UserService(UserRepository repository,
                       AccountService accountService,
                       ClientRepository clientRepository,
                       CardService cardService,
                       AgentService agentService) {
        this.repository = repository;
        this.clientRepository = clientRepository;
        this.accountService = accountService;
        this.cardService = cardService;
        this.agentService = agentService;
    }

    @Transactional
    public User save(User entity) {
        // Set created and updated timestamps if they're not already set
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());

        // Save the user to get the generated ID
        User savedUser = repository.save(entity);
        // If this is a client user, create a client record and account
        if ("client".equalsIgnoreCase(entity.getRole())) {
            Client client = createClientWithAccountAndBankNumber(savedUser);

            // Get the client's first account to link the card
            List<Account> accounts = accountService.getAccountsByClient(client.getId());
            if (!accounts.isEmpty()) {
                // Create a debit card for the user's primary account
                Account primaryAccount = accounts.get(0);
                createDefaultCardForAccount(primaryAccount, savedUser.getFirstName() + " " + savedUser.getLastName());

                // Create a Stripe Connect account for the user
//                createStripeConnectAccount(savedUser, client);
            }
        }
        else if ("BANK_EMPLOYEE".equalsIgnoreCase(entity.getRole())) {
            // Create an Agent record for bank employees
            Agent agent = new Agent();
            agent.setId(UUID.randomUUID().toString());
            agent.setUser(savedUser);
            agent.setFirstName(savedUser.getFirstName());
            agent.setLastName(savedUser.getLastName());
            agent.setEmail(savedUser.getEmail());
            agent.setPhone(savedUser.getPhoneNumber());

            // Generate a unique employee ID
            String employeeId = "EMP-" + LocalDateTime.now().getYear() + "-" +
                             String.format("%04d", new Random().nextInt(10000));
            agent.setEmployeeId(employeeId);

            // Set default values for bank employee
            agent.setBranch("Main Branch"); // Default branch, can be updated later
            agent.setRole("Junior Agent"); // Default role, can be updated later
            agent.setStatus("active");
            agent.setDateJoined(LocalDateTime.now());
            agent.setCreatedAt(LocalDateTime.now());
            agent.setUpdatedAt(LocalDateTime.now());

            // Save the agent
            agentService.save(agent);
        }

        return savedUser;
    }

    @Transactional
    protected Client createClientWithAccountAndBankNumber(User user) {
        // 1. Create a Client record
        Client client = new Client();
        client.setUser(user);
        client.setFirstName(user.getFirstName());
        client.setLastName(user.getLastName());
        client.setPhone(user.getPhoneNumber());
        client.setClientId(generateClientId());
        client.setStatus("active");
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());

        Client savedClient =clientRepository.save(client);

        // 2. Use EUR as default currency
        String defaultCurrency = "EUR";

        // 3. Create Account with the default currency
        // Note: The accountService.createAccount method handles all the account creation details
        Account savedAccount = accountService.createAccount(savedClient.getId(), defaultCurrency);
        // 4. AccountBankNumber is automatically created by createAccount method
        return savedClient;
    }

    private void createDefaultCardForAccount(Account account, String cardholderName) {
        Card card = new Card();
        card.setAccount(account);
        card.setCardholderName(cardholderName);

        // Set default card properties
        card.setType("debit");
        card.setNetwork("visa");

        // Generate masked card number (4 last digits visible)
        String cardNumber = generateCardNumber();
        card.setMaskedNumber("**** **** **** " + cardNumber.substring(12));

        // Set expiry date (3 years from now)
        LocalDateTime expiryDate = LocalDateTime.now().plusYears(3);
        card.setExpiryMonth(String.format("%02d", expiryDate.getMonthValue()));
        card.setExpiryYear(String.valueOf(expiryDate.getYear()));

        // Set default card settings
        card.setStatus("active");
        card.setIsContactless(true);
        card.setDailyLimit(new BigDecimal("1000.00"));
        card.setMonthlyLimit(new BigDecimal("5000.00"));
        card.setOnlinePaymentEnabled(true);
        card.setInternationalPaymentEnabled(false);
        card.setPinLocked(false);

        // Set timestamps
        LocalDateTime now = LocalDateTime.now();
        card.setCreatedAt(now);
        card.setUpdatedAt(now);

        // Save the card using the card service
        cardService.save(card);
    }

    /**
     * Create a Stripe Connect account for a new user
     * This automatically connects the user to Stripe without requiring any redirection
     */


    /**
     * Generates a unique client ID with the format CL-xxxxxxxx
     */
    private String generateClientId() {
        return "CL-" + UUID.randomUUID().toString().substring(0, 8);
    }

    /**
     * Generates a 10-digit account number
     */
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Generates a 16-digit card number
     */
    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public Optional<User> findById(String id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

}
