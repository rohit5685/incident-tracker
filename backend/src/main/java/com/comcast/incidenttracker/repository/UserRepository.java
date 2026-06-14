package com.comcast.incidenttracker.repository;

import com.comcast.incidenttracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Spring Data JPA auto-generates the SQL for this based on the method name:
    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);
}
