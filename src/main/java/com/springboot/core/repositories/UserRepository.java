package com.springboot.core.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.springboot.core.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String Email);

    Optional<User> findByEmail(String Email);

    @Query(value = "SELECT * FROM users " +
            "LIMIT :limit OFFSET :offset", nativeQuery = true)
    public List<User> getAllUsers(@Param("limit") int limit, @Param("offset") int offset);
}
