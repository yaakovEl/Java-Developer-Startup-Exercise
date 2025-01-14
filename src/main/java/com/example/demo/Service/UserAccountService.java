package com.example.demo.Service;

import com.example.demo.Model.UserAccount;
import com.example.demo.Repository.UserAccountRepository;
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

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    public ResponseEntity<?> getUserById(Long id) {
        Optional<UserAccount> user = userAccountRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    public ResponseEntity<?> createUser(UserAccount userAccount) {
        //System.out.println("Plain Password: " + userAccount.getPassword());
        if (userAccount.getPassword().length() <= 8 || userAccount.getPassword().length() >= 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password must be between 8 and 20 characters long.");
        }

        String encodedPassword = passwordEncoder.encode(userAccount.getPassword());
        System.out.println("Encoded Password: " + encodedPassword);
        userAccount.setPassword(encodedPassword);
        UserAccount savedUser = userAccountRepository.save(userAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    public ResponseEntity<?> updateUser(Long id, UserAccount updatedUser, String token) {

        String emailFromToken = jwtUtil.extractUsername(token);
        Optional<UserAccount> user = userAccountRepository.findById(id);
        if (user.isPresent()) {
            UserAccount existingUser = user.get();
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
