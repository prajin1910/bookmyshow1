package com.scenicairways.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.scenicairways.model.User;
import com.scenicairways.repository.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @PostConstruct
    public void createDefaultAdmin() {
        if (!userRepository.existsByUsername(adminUsername)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(User.Role.ADMIN);
            userRepository.save(admin);
            System.out.println("Default admin user created: " + adminUsername);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Try to find by email first, then by username for backward compatibility
        User user = userRepository.findByEmail(emailOrUsername)
                .orElse(userRepository.findByUsername(emailOrUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + emailOrUsername)));
        return user;
    }

    public User createUser(String username, String email, String password) {
        System.out.println("Creating user with username: " + username + ", email: " + email);
        
        if (userRepository.existsByUsername(username)) {
            System.out.println("Username already exists: " + username);
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            System.out.println("Email already exists: " + email);
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(User.Role.USER);

        try {
            User savedUser = userRepository.save(user);
            System.out.println("User created successfully with ID: " + savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            System.err.println("Error saving user: " + e.getMessage());
            throw new RuntimeException("Failed to create user: " + e.getMessage());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findByEmailOrUsername(String emailOrUsername) {
        return userRepository.findByEmail(emailOrUsername)
                .orElse(userRepository.findByUsername(emailOrUsername).orElse(null));
    }

    public long getUserCount() {
        return userRepository.count();
    }
}