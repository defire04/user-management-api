package com.example.service.user;


import com.example.domain.User;
import com.example.exception.user.UserWithThisEmailAlreadyExistsException;
import com.example.exception.user.UserWithThisIdNotFoundException;
import com.example.mapper.util.FieldsUpdaterMapper;
import com.example.repository.UserRepository;
import com.example.service.user.imp.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FieldsUpdaterMapper fieldsUpdaterMapper;


    @Test
    void testCreateUser_Success() {
        User newUser = new User()
                .setEmail("test@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(new Date())
                .setAddress("123 Main St")
                .setPhoneNumber("123456789");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User savedUser = userService.create(newUser);

        assertNotNull(savedUser.getId());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("123 Main St", savedUser.getAddress());
        assertEquals("123456789", savedUser.getPhoneNumber());
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        User existingUser = new User()
                .setEmail("existing@example.com")
                .setFirstName("Jane")
                .setLastName("Doe")
                .setBirthDate(new Date());
        existingUser.setId(1L);
        User newUser = new User()
                .setEmail("existing@example.com")
                .setFirstName("John")
                .setLastName("Doe")
                .setBirthDate(new Date());

        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(UserWithThisEmailAlreadyExistsException.class, () -> userService.create(newUser));
        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verify(userRepository, never()).save(newUser);
    }

    @Test
    void testUpdateUser_Success() {
        User userToUpdate = new User()
                .setEmail("updated@example.com")
                .setFirstName("John")
                .setLastName("Doe");
        userToUpdate.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.existsByEmailAndNotId("updated@example.com", 1L)).thenReturn(false);
        when(userRepository.save(userToUpdate)).thenReturn(userToUpdate);

        userService.update(userToUpdate);

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmailAndNotId("updated@example.com", 1L);
        verify(userRepository).save(userToUpdate);
    }

    @Test
    void testUpdate_UserNotFound() {
        User userToUpdate = new User()
                .setEmail("updated@example.com")
                .setFirstName("John")
                .setLastName("Doe");
        userToUpdate.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserWithThisIdNotFoundException.class, () -> userService.update(userToUpdate));

        verify(userRepository).findById(1L);
        verify(userRepository, never()).existsByEmailAndNotId(anyString(), anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        // Mock data
        User userToUpdate = new User()
                .setEmail("updated@example.com")
                .setFirstName("John")
                .setLastName("Doe");
        userToUpdate.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(userToUpdate));
        when(userRepository.existsByEmailAndNotId("updated@example.com", 1L)).thenReturn(true);

        assertThrows(UserWithThisEmailAlreadyExistsException.class, () -> userService.update(userToUpdate));

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmailAndNotId("updated@example.com", 1L);
        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void testUpdateSomeFields_Success() {

        User existingUser = new User()
                .setEmail("existing@example.com")
                .setFirstName("Jane")
                .setLastName("Doe");
        existingUser.setId(1L);

        User updatedFieldsUser = new User()
                .setEmail("updated@example.com")
                .setFirstName("John");
        updatedFieldsUser.setId(1L);

        User resultUser = new User()
                .setEmail("updated@example.com")
                .setFirstName("John")
                .setLastName("Doe");
        resultUser.setId(1L);


        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.existsByEmailAndNotId("updated@example.com", 1L)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(resultUser);
        when(fieldsUpdaterMapper.updateFields(existingUser, updatedFieldsUser)).thenReturn(resultUser);

        User updatedUser = userService.updateSomeFields(updatedFieldsUser);

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmailAndNotId("updated@example.com", 1L);
        verify(fieldsUpdaterMapper).updateFields(existingUser, updatedFieldsUser);
        verify(userRepository).save(resultUser);

        assertEquals(existingUser.getLastName(), updatedUser.getLastName());
    }

    @Test
    void testUpdateSomeFields_UserNotFound() {

        User updatedFieldsUser = new User()
                .setEmail("updated@example.com")
                .setFirstName("John");
        updatedFieldsUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(UserWithThisIdNotFoundException.class, () -> userService.updateSomeFields(updatedFieldsUser));

        verify(userRepository).findById(1L);
        verify(userRepository, never()).existsByEmailAndNotId(anyString(), anyLong());
        verify(fieldsUpdaterMapper, never()).updateFields(any(User.class), any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateSomeFields_EmailAlreadyExists() {
        User existingUser = new User()
                .setEmail("existing@example.com")
                .setFirstName("Jane")
                .setLastName("Doe");
        existingUser.setId(1L);

        User updatedFieldsUser = new User()
                .setEmail("updated@example.com")
                .setFirstName("John");
        updatedFieldsUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.existsByEmailAndNotId("updated@example.com", 1L)).thenReturn(true);

        assertThrows(UserWithThisEmailAlreadyExistsException.class, () -> userService.updateSomeFields(updatedFieldsUser));

        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmailAndNotId("updated@example.com", 1L);
        verify(fieldsUpdaterMapper, never()).updateFields(any(User.class), any(User.class));
        verify(userRepository, never()).save(any(User.class));
    }
    @Test
    void testDelete_Success() {
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(existingUser));
        userService.delete(userId);

        verify(userRepository).delete(existingUser);
    }

    @Test
    void testDelete_UserNotFound() {
        Long userId = 1L;

        assertThrows(UserWithThisIdNotFoundException.class, () -> userService.delete(userId));

        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testFindByBirthDateBetween_Success() {
        Date from = new Date(1049222400000L); // 2003-04-04
        Date to = new Date(1080758400000L); // 2004-04-04
        int size = 10;
        int page = 0;

        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        Page<User> expectedPage = new PageImpl<>(userList);

        when(userRepository.findByBirthDateBetween(from, to, PageRequest.of(page, size))).thenReturn(expectedPage);

        Page<User> resultPage = userService.findByBirthDateBetween(from, to, size, page);

        assertEquals(expectedPage, resultPage);
        verify(userRepository, times(1)).findByBirthDateBetween(from, to, PageRequest.of(page, size));
    }
}
