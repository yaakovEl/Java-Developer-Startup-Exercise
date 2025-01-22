package com.example.demo.service;

import com.example.demo.model.userAccount;
import com.example.demo.repository.UserAccountRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public List<userAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    public ResponseEntity<?> getUserById(Long id) {
        Optional<userAccount> user = userAccountRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    public ResponseEntity<?> createUser(userAccount userAccount) {
        String encodedPassword = passwordEncoder.encode(userAccount.getPassword());
        System.out.println("Encoded Password: " + encodedPassword);
        userAccount.setPassword(encodedPassword);
        com.example.demo.model.userAccount savedUser = userAccountRepository.save(userAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    public ResponseEntity<?> updateUser(Long id, userAccount updatedUser, String token) {

        String emailFromToken = jwtUtil.extractUsername(token);
        Optional<userAccount> user = userAccountRepository.findById(id);
        if (user.isPresent()) {
            userAccount existingUser = user.get();
            if (!existingUser.getEmail().equals(emailFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own account");
            }

            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            userAccountRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    public ResponseEntity<?> deleteUser(Long id) {
        if (userAccountRepository.existsById(id)) {
            userAccountRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
