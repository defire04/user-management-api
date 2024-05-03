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


    /**
     * Create a new user.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws UserWithThisEmailAlreadyExistsException If a user with this email already exists.
     */
    @NonNull
    @Transactional
    @Override
    public User create(User user) {
        checkIfEmailExists(user.getEmail());
        user.setId(null);
        return userRepository.save(user);
    }

    /**
     * Update all user information.
     *
     * @param user The user to update.
     * @return The updated user.
     * @throws UserWithThisIdNotFoundException        If no user with the given ID is found.
     * @throws UserWithThisEmailAlreadyExistsException If a user with the given email already exists.
     */
    @NonNull
    @Transactional
    @Override
    public User update(User user) {
        User existingUser = getUserById(user.getId());
        checkIfEmailExistsAndNotCurrentId(user.getEmail(), user.getId());
        return userRepository.save(user);
    }

    /**
     * Update some fields of a user.
     *
     * @param user The user with updated fields.
     * @return The updated user.
     * @throws UserWithThisIdNotFoundException        If no user with the given ID is found.
     * @throws UserWithThisEmailAlreadyExistsException If a user with the given email already exists.
     */
    @NonNull
    @Transactional
    @Override
    public User updateSomeFields(User user) {
        User existingUser = getUserById(user.getId());
        checkIfEmailExistsAndNotCurrentId(user.getEmail(), user.getId());
        return userRepository.save(fieldsUpdaterMapper.updateFields(existingUser, user));
    }

    /**
     * Delete a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @throws UserWithThisIdNotFoundException If no user with the given ID is found.
     */
    @Transactional
    @Override
    public void delete(Long userId) {
        userRepository.delete(getUserById(userId));
    }

    /**
     * Find users by birth date within a range.
     *
     * @param from The start date of the range.
     * @param to   The end date of the range.
     * @param size The page size.
     * @param page The page number.
     * @return A page of users.
     */
    @NonNull
    @Override
    public Page<User> findByBirthDateBetween(Date from, Date to, int size, int page) {
        return userRepository.findByBirthDateBetween(from, to, PageRequest.of(page, size));
    }

    /**
     * Get a user by ID or throw an exception if the user is not found.
     *
     * @param userId The ID of the user.
     * @return The user with the specified ID.
     * @throws UserWithThisIdNotFoundException If no user with the given ID is found.
     */
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserWithThisIdNotFoundException(userId));
    }

    /**
     * Check if a user with the given email already exists.
     *
     * @param email The email to check.
     * @throws UserWithThisEmailAlreadyExistsException If a user with the given email already exists.
     */
    private void checkIfEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserWithThisEmailAlreadyExistsException();
        }
    }

    /**
     * Check if a user with the given email exists but has a different ID.
     *
     * @param email The email to check.
     * @param id    The ID to exclude from the check.
     * @throws UserWithThisEmailAlreadyExistsException If a user with the given email already exists but has a different ID.
     */
    private void checkIfEmailExistsAndNotCurrentId(String email, Long id) {
        if (userRepository.existsByEmailAndNotId(email, id)) {
            throw new UserWithThisEmailAlreadyExistsException();
        }
    }
}