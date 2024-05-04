package com.example.service.user;

import com.example.domain.User;
import com.example.exception.user.UserWithThisEmailAlreadyExistsException;
import com.example.exception.user.UserWithThisIdNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface IUserService {

    /**
     * Create a new user.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws UserWithThisEmailAlreadyExistsException If a user with this email already exists.
     */
    @NonNull
    @Transactional
    User create(User user);

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
    User update(User user);

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
    User updateSomeFields(User user);

    /**
     * Delete a user by ID.
     *
     * @param userId The ID of the user to delete.
     * @throws UserWithThisIdNotFoundException If no user with the given ID is found.
     */
    @Transactional
    void delete(Long userId);

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
    Page<User> findByBirthDateBetween(Date from, Date to, int size, int page);


}
