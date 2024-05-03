package com.example.controller;

import com.example.domain.User;
import com.example.dto.response.ResponseDTO;
import com.example.dto.response.ResponseListDTO;
import com.example.dto.user.UserDTO;
import com.example.mapper.user.IUserMapper;
import com.example.service.user.IUserService;
import com.fasterxml.jackson.annotation.JsonView;
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
    
    

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDTO<UserDTO> createUser(@Valid @RequestBody @JsonView({UserDTO.Input.class}) UserDTO userDTO) {
        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(userService.create(userMapper.toModel(userDTO))))
                .build();
    }

    @PutMapping("/{userId}")
    public ResponseDTO<UserDTO> updateUser(@PathVariable Long userId,
                                           @Valid @RequestBody @JsonView({UserDTO.Input.class}) UserDTO userDTO) {
        userDTO.setId(userId);
        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(userService.update(userMapper.toModel(userDTO))))
                .build();
    }

    @PatchMapping("/{userId}")
    public ResponseDTO<UserDTO> updateSomeUserFields(@PathVariable Long userId,
                                                     @Valid @RequestBody @JsonView({UserDTO.InputSomeFields.class}) UserDTO userDTO) {
        userDTO.setId(userId);
        return ResponseDTO.<UserDTO>builder()
                .data(userMapper.toDTO(userService.updateSomeFields(userMapper.toModel(userDTO))))
                .build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseListDTO<List<UserDTO>> getUsersByBirthDateRange( @RequestParam(required = false, defaultValue = "0") int page,
                                                                    @RequestParam(required = false, defaultValue = "5") int size,
                                                                    @RequestParam Long from,
                                                                    @RequestParam Long to){
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
