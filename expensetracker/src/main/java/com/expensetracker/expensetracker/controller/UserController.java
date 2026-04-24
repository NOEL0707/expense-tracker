package com.expensetracker.expensetracker.controller;

import com.expensetracker.expensetracker.dto.UpsertUserDTO;
import com.expensetracker.expensetracker.dto.UserResponseDTO;
import com.expensetracker.expensetracker.model.AppUser;
import com.expensetracker.expensetracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for managing mock and application users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get list of users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return ResponseEntity.ok(userService.getUsers().stream()
                .map(this::toResponse)
                .toList());
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a single user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(toResponse(userService.getUser(userId)));
    }

    @PostMapping
    @Operation(summary = "Create a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UpsertUserDTO dto) {
        AppUser user = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(user));
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID userId, @Valid @RequestBody UpsertUserDTO dto) {
        return ResponseEntity.ok(toResponse(userService.updateUser(userId, dto)));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user and their expenses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    private UserResponseDTO toResponse(AppUser user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
