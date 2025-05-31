package org.example.ebanking1.Config;
import org.example.ebanking1.controller.*;
import org.example.ebanking1.service.*;
import org.example.ebanking1.repositories.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@PropertySource("classpath:app.properties")
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.example.ebanking1.repositories")
@ComponentScan(basePackages = {"org.example.ebanking1.service", "org.example.ebanking1.repositories", "org.example.ebanking1.controller"})
public class AppConfig implements WebMvcConfigurer {

    @Value("${db.driver}")
    private String driver;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    // Infrastructure beans
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("org.example.ebanking1.entities");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(true);
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.hbm2ddl.auto", "update"); // Changed from 'update'  create-drop to 'create'
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false");
        em.setJpaProperties(properties);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    // SERVICE BEANS

    // Account & banking related services
    @Bean
    public AccountService accountService(AccountRepository accountRepository,
                                         ClientRepository clientRepository,
                                         AccountBankNumberRepository accountBankNumberRepository,
                                         AccountBankNumberService accountBankNumberService) {
        return new AccountService(accountRepository, clientRepository, accountBankNumberRepository, accountBankNumberService);
    }

    @Bean
    public AccountBankNumberService accountBankNumberService(AccountBankNumberRepository repository) {
        return new AccountBankNumberService(repository);
    }

    @Bean
    public BankingProductService bankingProductService(BankingProductRepository repository) {
        return new BankingProductService(repository);
    }

    @Bean
    public ProductApplicationService productApplicationService(ProductApplicationRepository repository) {
        return new ProductApplicationService(repository);
    }

    // User & client related services
    @Bean
    public UserService userService(UserRepository userRepository,
                                   ClientRepository clientRepository,
                                   AccountService accountService,
                                   CardService cardService,
                                   AgentService agentService
                                  ) {
        return new UserService(userRepository, accountService,clientRepository, cardService, agentService);
    }


@Bean
    public StripeConnectService stripeConnectService(UserRepository userRepository,
                                                     Striperepo  stripeRepository
                                                     ) {
        return new StripeConnectService( userRepository, stripeRepository);
    }
    @Bean
    public StripeConnectController stripeConnectController(StripeConnectService stripeConnectService) {
        return new StripeConnectController(stripeConnectService);
    }
    @Bean
    public ClientService clientService(ClientRepository clientRepository, UserRepository userRepository) {
        return new ClientService(clientRepository, userRepository);
    }

    @Bean
    public AgentService agentService(AgentRepository agentRepository) {
        return new AgentService(agentRepository);
    }


    @Bean
    public PaymentMethodService paymentMethodService(PaymentMethodRepository repository) {
        return new PaymentMethodService(repository);
    }

    // Card related services
    @Bean
    public CardService cardService(CardRepository repository) {
        return new CardService(repository);
    }

//    @Bean
//    public CardRequestService cardRequestService(CardRequestRepository repository) {
//        return new CardRequestService(repository);
//    }



    // Communication services
    @Bean
    public AlertService alertService(AlertSettingRepository repository) {
        return new AlertService(repository);
    }

    @Bean
    public NotificationService notificationService(NotificationRepository repository) {
        return new NotificationService(repository);
    }

    @Bean
    public DocumentService documentService(DocumentRepository repository,AccountService accountService) {
        return new DocumentService(repository,accountService);
    }

    @Bean
    public AnnouncementService announcementService(AnnouncementRepository repository) {
        return new AnnouncementService(repository);
    }

    // Business related services
    @Bean
    public RechargeProviderService rechargeProviderService(RechargeProviderRepository repository) {
        return new RechargeProviderService(repository);
    }




    // CONTROLLER BEANS

    // Account & banking related controllers
    @Bean
    public AccountController accountController(AccountService accountService) {
        return new AccountController(accountService);
    }

    @Bean
    public AccountBankNumberController accountBankNumberController(AccountBankNumberService service) {
        return new AccountBankNumberController(service);
    }

    @Bean
    public BankingProductController bankingProductController(BankingProductService service) {
        return new BankingProductController(service);
    }

    @Bean
    public ProductApplicationController productApplicationController(ProductApplicationService service) {
        return new ProductApplicationController(service);
    }

    // User & client related controllers
   @Bean
    public UserController userController(UserService userService, AccountService accountService) {
        return new UserController(userService, accountService);
    }

    @Bean
    public ClientController clientController(ClientService clientService) {
        return new ClientController(clientService);
    }

    @Bean
    public AgentController agentController(AgentService agentService) {
        return new AgentController(agentService);
    }


    @Bean
    public PaymentMethodController paymentMethodController(PaymentMethodService service) {
        return new PaymentMethodController(service);
    }

    // Card related controllers
    @Bean
    public CardController cardController(CardService service) {
        return new CardController(service);
    }


//    @Bean
//    public CurrencyController currencyController(CurrencyService service) {
//        return new CurrencyController(service);
//    }

    // Communication controllers
    @Bean
    public AlertController alertController(AlertService service) {
        return new AlertController(service);
    }

    @Bean
    public NotificationController notificationController(NotificationService service) {
        return new NotificationController(service);
    }


    @Bean
    public DocumentController documentController(DocumentService service) {
        return new DocumentController(service);
    }

    @Bean
    public AnnouncementController announcementController(AnnouncementService service) {
        return new AnnouncementController(service);
    }

    // Business related controllers
    @Bean
    public RechargeProviderController rechargeProviderController(RechargeProviderService service) {
        return new RechargeProviderController(service);
    }

    // Transaction related controllers
    @Bean
    public TransferController transferController(TransferService service) {
        return new TransferController(service);
    }

    @Bean
    public TransactionController transactionController(StripeTransactionService service) {
        return new TransactionController(service);
    }



//    // Crypto related controllers
//    @Bean
//    public CryptoAssetController cryptoAssetController(CryptoAssetService service) {
//        return new CryptoAssetController(service);
//    }
//
//    @Bean
//    public CryptoTransactionController cryptoTransactionController(CryptoTransactionService service) {
//        return new CryptoTransactionController(service);
//    }
//
//    @Bean
//    public CryptoWalletController cryptoWalletController(CryptoWalletService service) {
//        return new CryptoWalletController(service);
//    }
}
