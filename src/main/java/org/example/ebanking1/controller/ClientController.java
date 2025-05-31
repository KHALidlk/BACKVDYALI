package org.example.ebanking1.controller;

import org.example.ebanking1.dto.ClientDto;
import org.example.ebanking1.dto.ClientMapper;
import org.example.ebanking1.entities.Client;
import org.example.ebanking1.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(ClientMapper.toDTOList(clients));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable("id") String id) {
        return clientService.getClientById(id)
                .map(client -> ResponseEntity.ok(ClientMapper.toDTO(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/client-id/{clientId}")
    public ResponseEntity<ClientDto> getClientByClientId(@PathVariable("clientId") String clientId) {
        return clientService.getClientByClientId(clientId)
                .map(client -> ResponseEntity.ok(ClientMapper.toDTO(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ClientDto>> getClientsByStatus(@PathVariable("status") String status) {
        List<Client> clients = clientService.getClientsByStatus(status);
        return ResponseEntity.ok(ClientMapper.toDTOList(clients));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ClientDto> createClient(@PathVariable("usersId") String userId) {
        Client client = clientService.createClient(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ClientMapper.toDTO(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable("id") String id, @RequestBody ClientDto clientDto) {
        // Get the existing client
        Client existingClient = clientService.getClientById(id)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + id));

        // Update the client entity from the DTO
        Client clientToUpdate = ClientMapper.updateEntityFromDto(existingClient, clientDto);

        // Save the updated client
        Client updatedClient = clientService.updateClient(id, clientToUpdate);

        // Return the updated client as a DTO
        return ResponseEntity.ok(ClientMapper.toDTO(updatedClient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable("id")  String id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/verify")
    public ResponseEntity<ClientDto> verifyClient(@PathVariable String id) {
        clientService.verifyClient(id);
        return clientService.getClientById(id)
                .map(client -> ResponseEntity.ok(ClientMapper.toDTO(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ClientDto> updateClientStatus(@PathVariable String id, @RequestParam String status) {
        clientService.updateClientStatus(id, status);
        return clientService.getClientById(id)
                .map(client -> ResponseEntity.ok(ClientMapper.toDTO(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ClientDto> getClientByUserId(@PathVariable String userId) {
        return clientService.findByUserId(userId)
                .map(client -> ResponseEntity.ok(ClientMapper.toDTO(client)))
                .orElse(ResponseEntity.notFound().build());
    }
}

