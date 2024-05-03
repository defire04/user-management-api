package com.example.repository;

import com.example.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByBirthDateBetween(Date from, Date to, PageRequest pageRequest);

    Boolean existsByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = ?1 AND u.id <> ?2")
    Boolean existsByEmailAndNotId(String email, Long id);
}
