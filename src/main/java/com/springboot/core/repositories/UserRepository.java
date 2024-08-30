package com.springboot.core.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.core.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String Email);

    Optional<User> findByEmail(String Email);

}
