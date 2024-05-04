package com.example.service.user.imp;

import com.example.domain.User;
import com.example.exception.user.UserWithThisEmailAlreadyExistsException;
import com.example.exception.user.UserWithThisIdNotFoundException;
import com.example.mapper.util.FieldsUpdaterMapper;
import com.example.repository.UserRepository;
import com.example.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    private final FieldsUpdaterMapper fieldsUpdaterMapper;

    @NonNull
    @Transactional
    @Override
    public User create(User user) {
        checkIfEmailExists(user.getEmail());
        user.setId(null);
        return userRepository.save(user);
    }

    @NonNull
    @Transactional
    @Override
    public User update(User user) {
        User existingUser = getUserById(user.getId());
        checkIfEmailExistsAndNotCurrentId(user.getEmail(), user.getId());
        return userRepository.save(user);
    }

    @NonNull
    @Transactional
    @Override
    public User updateSomeFields(User user) {
        User existingUser = getUserById(user.getId());
        checkIfEmailExistsAndNotCurrentId(user.getEmail(), user.getId());
        return userRepository.save(fieldsUpdaterMapper.updateFields(existingUser, user));
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        userRepository.delete(getUserById(userId));
    }


    @NonNull
    @Override
    public Page<User> findByBirthDateBetween(Date from, Date to, int size, int page) {
        return userRepository.findByBirthDateBetween(from, to, PageRequest.of(page, size));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserWithThisIdNotFoundException(userId));
    }


    private void checkIfEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserWithThisEmailAlreadyExistsException();
        }
    }


    private void checkIfEmailExistsAndNotCurrentId(String email, Long id) {
        if (userRepository.existsByEmailAndNotId(email, id)) {
            throw new UserWithThisEmailAlreadyExistsException();
        }
    }
}