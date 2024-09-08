package com.springboot.core.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.core.dtos.UserDto;
import com.springboot.core.models.User;
import com.springboot.core.repositories.UserRepository;

import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public List<User> getAllUsers(int limit, int offset) {
        if (limit == -1) {
            return userRepository.findAll();
        } else {
            return userRepository.getAllUsers(limit, offset);
        }
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Null deleteUserById(Long id) {
        userRepository.deleteById(id);
        return null;
    }
}
