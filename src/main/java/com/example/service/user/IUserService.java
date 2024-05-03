package com.example.service.user;

import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public interface IUserService {

    @NonNull
    @Transactional
    User create(User user);

    @NonNull
    @Transactional
    User update(User user);

    @NonNull
    @Transactional
    User updateSomeFields(User user);

    @Transactional
    void delete(Long userId);

    @NonNull
    Page<User> findByBirthDateBetween(Date from, Date to, int size, int page);


}
