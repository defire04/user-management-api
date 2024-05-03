package com.example.controller;

import com.example.domain.User;
import com.example.dto.user.UserDTO;
import com.example.mapper.user.IUserMapper;
import com.example.service.user.IUserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {


    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @Mock
    private IUserMapper userMapper;

    @InjectMocks
    private UserController userController;


    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser() throws Exception {
        UserDTO userDTO = new UserDTO()
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(new Date(1049222400000L)) // 2003-04-04
                .setAddress("123 Main St")
                .setPhoneNumber("1234567890");


        String userJson = objectMapper.writeValueAsString(userDTO);


        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated());

    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO()
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(new Date(1049222400000L)) // 2003-04-04
                .setAddress("123 Main St")
                .setPhoneNumber("1234567890")
                .setId(userId);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setEmail(userDTO.getEmail());
        updatedUser.setFirstName(userDTO.getFirstName());
        updatedUser.setLastName(userDTO.getLastName());

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(updatedUser.getId());
        updatedUserDTO.setEmail(updatedUser.getEmail());
        updatedUserDTO.setFirstName(updatedUser.getFirstName());
        updatedUserDTO.setLastName(updatedUser.getLastName());

        when(userMapper.toModel(any(UserDTO.class))).thenReturn(updatedUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(updatedUserDTO);
        when(userService.update(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(updatedUserDTO.getId()))
                .andExpect(jsonPath("$.data.email").value(updatedUserDTO.getEmail()))
                .andExpect(jsonPath("$.data.firstName").value(updatedUserDTO.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(updatedUserDTO.getLastName()));

        verify(userMapper).toModel(any(UserDTO.class));
        verify(userMapper).toDTO(any(User.class));
        verify(userService).update(any(User.class));
    }

    @Test
    void testUpdateSomeUserFields() throws Exception {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO()
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(new Date(1049222400000L)) // 2003-04-04
                .setId(userId);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setFirstName(userDTO.getFirstName());
        updatedUser.setLastName(userDTO.getLastName());

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(updatedUser.getId());
        updatedUserDTO.setFirstName(updatedUser.getFirstName());
        updatedUserDTO.setLastName(updatedUser.getLastName());


        when(userMapper.toModel(any(UserDTO.class))).thenReturn(updatedUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(updatedUserDTO);
        when(userService.updateSomeFields(any(User.class))).thenReturn(updatedUser);


        mockMvc.perform(patch("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id").value(updatedUserDTO.getId()))
                .andExpect(jsonPath("$.data.firstName").value(updatedUserDTO.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(updatedUserDTO.getLastName()));


        verify(userMapper).toModel(any(UserDTO.class));
        verify(userMapper).toDTO(any(User.class));
        verify(userService).updateSomeFields(any(User.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;

        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testGetUsersByBirthDateRange() throws Exception {
        long from = Instant.parse("2000-01-01T00:00:00Z").toEpochMilli();
        long to = Instant.parse("2000-12-31T23:59:59Z").toEpochMilli();
        int page = 0;
        int size = 5;

        List<User> userList = Arrays.asList(
                new User(), new User(), new User()
        );
        Page<User> pageData = new PageImpl<>(userList);

        when(userService.findByBirthDateBetween(any(Date.class), any(Date.class), eq(size), eq(page)))
                .thenReturn(pageData);

        mockMvc.perform(get("/users/search")
                        .param("from", String.valueOf(from))
                        .param("to", String.valueOf(to))
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(userList.size())));

        verify(userService, times(1)).findByBirthDateBetween(any(Date.class), any(Date.class), eq(size), eq(page));
    }
}
