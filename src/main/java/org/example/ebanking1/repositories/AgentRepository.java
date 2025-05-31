package org.example.ebanking1.repositories;

import org.example.ebanking1.entities.Agent;
import org.example.ebanking1.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, String> {
    Optional<Agent> findByEmployeeId(String employeeId);
    List<Agent> findByStatus(String status);
    List<Agent> findByBranch(String branch);
    Optional<Agent> findByUser(User user);
}