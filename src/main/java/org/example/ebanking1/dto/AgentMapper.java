package org.example.ebanking1.dto;

import org.example.ebanking1.entities.Agent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class to convert between Agent entities and DTOs
 */
public class AgentMapper {

    /**
     * Convert Agent entity to AgentDto
     * @param agent The agent entity to convert
     * @return AgentDto representing the agent
     */
    public static AgentDto toDTO(Agent agent) {
        if (agent == null) {
            return null;
        }

        AgentDto dto = new AgentDto();
        dto.setId(agent.getId());
        dto.setFirstName(agent.getFirstName());
        dto.setLastName(agent.getLastName());
        dto.setEmail(agent.getEmail());
        dto.setPhone(agent.getPhone());
        dto.setEmployeeId(agent.getEmployeeId());
        dto.setBranch(agent.getBranch());
        dto.setRole(agent.getRole());
        dto.setStatus(agent.getStatus());
        dto.setImageUrl(agent.getImageUrl());
        dto.setDateJoined(agent.getDateJoined());
        dto.setLastLogin(agent.getLastLogin());
        dto.setCreatedAt(agent.getCreatedAt());
        dto.setUpdatedAt(agent.getUpdatedAt());

        // Map user information if available
        if (agent.getUser() != null) {
            dto.setUserId(agent.getUser().getId());
            dto.setUsername(agent.getUser().getFullName());
            dto.setUserEmail(agent.getUser().getEmail());
        }

        return dto;
    }

    /**
     * Convert a list of Agent entities to a list of AgentDtos
     * @param agents The list of agent entities to convert
     * @return List of AgentDto objects
     */
    public static List<AgentDto> toDTOList(List<Agent> agents) {
        if (agents == null) {
            return Collections.emptyList();
        }

        return agents.stream()
                .map(AgentMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update an existing Agent entity from an AgentDto
     * This doesn't set all fields, only those that are editable by users
     * @param agent The agent entity to update
     * @param dto The DTO containing the new values
     * @return The updated agent entity (not yet persisted)
     */
    public static Agent updateEntityFromDto(Agent agent, AgentDto dto) {
        if (agent == null || dto == null) {
            return agent;
        }

        // Only update fields that should be editable
        if (dto.getFirstName() != null) agent.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) agent.setLastName(dto.getLastName());
        if (dto.getEmail() != null) agent.setEmail(dto.getEmail());
        if (dto.getPhone() != null) agent.setPhone(dto.getPhone());
        if (dto.getBranch() != null) agent.setBranch(dto.getBranch());
        if (dto.getRole() != null) agent.setRole(dto.getRole());
        if (dto.getImageUrl() != null) agent.setImageUrl(dto.getImageUrl());

        return agent;
    }
}
