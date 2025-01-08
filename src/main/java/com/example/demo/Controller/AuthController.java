package com.example.demo.Controller;

import com.example.demo.Model.UserAccount;
import com.example.demo.Repository.UserAccountRepository;
import com.example.demo.dto.LoginRequest;
import com.example.demo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // בדיקה אם כתובת האימייל והסיסמה נשלחו
        if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("Email and password are required");
        }

        // חיפוש משתמש לפי אימייל
        UserAccount user = userAccountRepository.findByEmail(loginRequest.getEmail());

        // אם המשתמש לא נמצא או שהסיסמה אינה נכונה
        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // יצירת טוקן JWT (אם אתה משתמש ב-JWT)
        String token = jwtUtil.generateToken(user.getEmail());

        // החזרת טוקן כהצלחה
        return ResponseEntity.ok(token);
    }
    @PostMapping("/update-passwords")
    public ResponseEntity<?> updatePasswordsToEncrypted() {
        List<UserAccount> users = userAccountRepository.findAll();

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (UserAccount user : users) {
            // בדיקה אם הסיסמה עדיין לא מוצפנת
            if (!user.getPassword().startsWith("$2")) {
                String encryptedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encryptedPassword);
                userAccountRepository.save(user);
            }
        }
        return ResponseEntity.ok("Passwords updated successfully!");
    }
    @PostMapping("/fix-passwords")
    public ResponseEntity<?> fixPasswords() {
        List<UserAccount> users = userAccountRepository.findAll();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        for (UserAccount user : users) {
            if (user.getPassword().length() < 8 || user.getPassword().length() > 20) {
                String defaultPassword = "DefaultPass123"; // סיסמה ברירת מחדל
                String encryptedPassword = passwordEncoder.encode(defaultPassword);
                user.setPassword(encryptedPassword);
                userAccountRepository.save(user);
            }
        }

        return ResponseEntity.ok("Passwords fixed successfully!");
    }


}
