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
        // Print the plain password (for debugging; remove in production)
        System.out.println("Plain Password: " + userAccount.getPassword());

        // Validate password length
        if (userAccount.getPassword().length() <= 8 || userAccount.getPassword().length() >= 20) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Password must be between 8 and 20 characters long.");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(userAccount.getPassword());
        System.out.println("Encoded Password: " + encodedPassword);
        userAccount.setPassword(encodedPassword);

        // Save the user to the database
        UserAccount savedUser = userAccountRepository.save(userAccount);

        // Return the created user with HTTP 201 status
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    public ResponseEntity<?> updateUser(Long id, UserAccount updatedUser, String token) {
        // שליפת ה-email מתוך ה-JWT
        String emailFromToken = jwtUtil.extractUsername(token);

        // שליפת המשתמש לפי ID
        Optional<UserAccount> user = userAccountRepository.findById(id);
        if (user.isPresent()) {
            UserAccount existingUser = user.get();

            // בדיקה אם ה-email מהטוקן תואם ל-email של המשתמש
            if (!existingUser.getEmail().equals(emailFromToken)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own account");
            }

            // עדכון השדות
            if (updatedUser.getFirstName() != null) {
                existingUser.setFirstName(updatedUser.getFirstName());
            }
            if (updatedUser.getLastName() != null) {
                existingUser.setLastName(updatedUser.getLastName());
            }
            if (updatedUser.getPassword() != null) {
                existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }

            // שמירה בבסיס הנתונים
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
