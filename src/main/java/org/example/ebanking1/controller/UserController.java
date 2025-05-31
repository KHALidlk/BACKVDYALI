package org.example.ebanking1.controller;

import org.example.ebanking1.dto.UserDTO;
import org.example.ebanking1.entities.User;
import org.example.ebanking1.service.AccountBankNumberService;
import org.example.ebanking1.service.AccountService;
import org.example.ebanking1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
private final AccountService accountService;
    @Autowired
    public UserController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;

    }
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDtos = userService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        return userService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return userService.findByEmail(email)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto) {
        User user = convertToEntity(userDto);
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(savedUser));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDto) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        User user = convertToEntity(userDto);
        user.setId(id); // Ensure the ID is set
        user.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(convertToDto(userService.save(user)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (userService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable String id,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        // Implement password validation logic
        if (!oldPassword.equals(user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        user.setPasswordHash(newPassword);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/gdpr-consent")
    public ResponseEntity<Void> updateGdprConsent(
            @PathVariable String id,
            @RequestParam boolean consent) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.setGdprConsent(consent);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/two-factor")
    public ResponseEntity<Void> updateTwoFactorSettings(
            @PathVariable String id,
            @RequestParam boolean enabled,
            @RequestParam String method) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        user.setTwoFactorEnabled(enabled);
        user.setTwoFactorMethod(method);
        userService.save(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/login")
    public ResponseEntity<Void> recordLogin(@PathVariable String id) {
        // This method is not implemented in the UserService
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
    private UserDTO convertToDto(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        userDto.setRole(user.getRole());
        userDto.setIsActive(user.getIsActive());
        userDto.setLanguage(user.getLanguage());
        userDto.setGdprConsent(user.getGdprConsent());
        userDto.setGdprConsentDate(user.getGdprConsentDate());
        userDto.setIdentityType(user.getIdentityType());
        userDto.setIdentityNumber(user.getIdentityNumber());
        userDto.setTwoFactorEnabled(user.getTwoFactorEnabled());
        userDto.setTwoFactorMethod(user.getTwoFactorMethod());
        userDto.setLastLogin(user.getLastLogin());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        // We don't copy the password for security reasons when converting to DTO
        return userDto;
    }

    private User convertToEntity(UserDTO userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRole(userDto.getRole());
        user.setIsActive(userDto.getIsActive());
        user.setLanguage(userDto.getLanguage());
        user.setGdprConsent(userDto.getGdprConsent());
        user.setGdprConsentDate(userDto.getGdprConsentDate());
        user.setIdentityType(userDto.getIdentityType());
        user.setIdentityNumber(userDto.getIdentityNumber());
        user.setTwoFactorEnabled(userDto.getTwoFactorEnabled());
        user.setTwoFactorMethod(userDto.getTwoFactorMethod());
        user.setLastLogin(userDto.getLastLogin());
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
        // Copy password hash only when it's provided (for create/update operations)
        if (userDto.getPasswordHash() != null) {
            user.setPasswordHash(userDto.getPasswordHash());
        }
        return user;
    }
}
