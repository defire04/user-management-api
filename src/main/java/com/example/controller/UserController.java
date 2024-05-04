package com.example.controller;

import com.example.domain.User;
import com.example.dto.response.ResponseDTO;
import com.example.dto.response.ResponseListDTO;
import com.example.dto.user.UserDTO;
import com.example.mapper.user.IUserMapper;
import com.example.service.user.IUserService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {


    private final IUserService userService;

    private final IUserMapper userMapper;

    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<UserDTO> createUser(@Valid @RequestBody @JsonView({UserDTO.Input.class}) UserDTO userDTO) {
        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(userService.create(userMapper.toModel(userDTO))))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @Operation(summary = "Update an existing user")
    @PutMapping("/{userId}")
    public ResponseDTO<UserDTO> updateUser(@Parameter(description = "ID of the user to update") @PathVariable Long userId,
                                           @Valid @RequestBody @JsonView({UserDTO.Input.class}) UserDTO userDTO) {
        userDTO.setId(userId);
        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(userService.update(userMapper.toModel(userDTO))))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @Operation(summary = "Update some fields of an existing user")
    @PatchMapping("/{userId}")
    public ResponseDTO<UserDTO> updateSomeUserFields(@Parameter(description = "ID of the user to update") @PathVariable Long userId,
                                                     @Valid @RequestBody @JsonView({UserDTO.InputSomeFields.class}) UserDTO userDTO) {
        userDTO.setId(userId);
        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(userService.updateSomeFields(userMapper.toModel(userDTO))))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Not found"),
    })
    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
    })
    @Operation(summary = "Get users by birth date range")
    @GetMapping("/search")
    public ResponseListDTO<List<UserDTO>> getUsersByBirthDateRange(@Parameter(description = "Page number (default is 0)")
                                                                   @RequestParam(required = false, defaultValue = "0") int page,
                                                                   @Parameter(description = "Page size (default is 5)")
                                                                   @RequestParam(required = false, defaultValue = "5") int size,
                                                                   @Parameter(description = "Start date for the range")
                                                                   @RequestParam Long from,
                                                                   @Parameter(description = "End date for the range")
                                                                   @RequestParam Long to) {
        Page<User> pageData = userService.findByBirthDateBetween(new Date(from), new Date(to), size, page);

        List<UserDTO> categoryDTOs = pageData.getContent().stream()
                .map(userMapper::toDTO)
                .toList();

        return ResponseListDTO.<List<UserDTO>>builder()
                .currentPage(pageData.getNumber())
                .size(pageData.getSize())
                .data(categoryDTOs)
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();

    }
}
