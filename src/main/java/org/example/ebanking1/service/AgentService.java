package org.example.ebanking1.service;

import org.example.ebanking1.entities.Agent;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.repositories.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AgentService {

    private final AgentRepository repository;

    @Autowired
    public AgentService(AgentRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Agent save(Agent entity) {
        return repository.save(entity);
    }

    public Optional<Agent> findById(String id) {
        return repository.findById(id);
    }

    public List<Agent> findAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(String id) {
        repository.deleteById(id);
    }

    public Optional<Agent> findByEmployeeId(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public List<Agent> findByStatus(String status) {
        return repository.findByStatus(status);
    }

    public List<Agent> findByBranch(String branch) {
        return repository.findByBranch(branch);
    }

    public Optional<Agent> findByUser(User user) {
        return repository.findByUser(user);
    }
public String genetateAgentId() {
        String prefix = "AGT";
        String suffix = String.valueOf(System.currentTimeMillis());
        return prefix + suffix;
    }
    @Transactional
    public Agent updateAgentStatus(String id, String status) {
        Agent agent = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + id));
        agent.setStatus(status);
        return repository.save(agent);
    }
}
