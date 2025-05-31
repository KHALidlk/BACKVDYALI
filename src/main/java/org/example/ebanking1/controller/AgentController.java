package org.example.ebanking1.controller;

import org.example.ebanking1.dto.AgentDto;
import org.example.ebanking1.dto.AgentMapper;
import org.example.ebanking1.entities.Agent;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService service;

    @Autowired
    public AgentController(AgentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgentDto> save(@RequestBody Agent entity) {
        Agent savedAgent = service.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(AgentMapper.toDTO(savedAgent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDto> findById(@PathVariable("id") String id) {
        Optional<Agent> agent = service.findById(id);
        return agent.map(a -> ResponseEntity.ok(AgentMapper.toDTO(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AgentDto>> findAll() {
        List<Agent> agents = service.findAll();
        return ResponseEntity.ok(AgentMapper.toDTOList(agents));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<AgentDto> findByEmployeeId(@PathVariable String employeeId) {
        Optional<Agent> agent = service.findByEmployeeId(employeeId);
        return agent.map(a -> ResponseEntity.ok(AgentMapper.toDTO(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AgentDto>> findByStatus(@PathVariable String status) {
        List<Agent> agents = service.findByStatus(status);
        return ResponseEntity.ok(AgentMapper.toDTOList(agents));
    }

    @GetMapping("/branch/{branch}")
    public ResponseEntity<List<AgentDto>> findByBranch(@PathVariable String branch) {
        List<Agent> agents = service.findByBranch(branch);
        return ResponseEntity.ok(AgentMapper.toDTOList(agents));
    }

    @GetMapping("/user")
    public ResponseEntity<AgentDto> findByUser(@RequestBody User user) {
        Optional<Agent> agent = service.findByUser(user);
        return agent.map(a -> ResponseEntity.ok(AgentMapper.toDTO(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<AgentDto> updateAgentStatus(
            @PathVariable String id,
            @PathVariable String status) {
        try {
            Agent updated = service.updateAgentStatus(id, status);
            return ResponseEntity.ok(AgentMapper.toDTO(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDto> updateAgent(@PathVariable String id, @RequestBody AgentDto agentDto) {
        try {
            // Get the existing agent
            Optional<Agent> existingAgentOpt = service.findById(id);
            if (existingAgentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Update the agent entity from the DTO
            Agent existingAgent = existingAgentOpt.get();
            Agent updatedAgent = AgentMapper.updateEntityFromDto(existingAgent, agentDto);

            // Save the updated agent
            Agent savedAgent = service.save(updatedAgent);

            // Return the updated agent as a DTO
            return ResponseEntity.ok(AgentMapper.toDTO(savedAgent));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
