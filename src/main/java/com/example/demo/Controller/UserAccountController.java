package com.example.demo.Controller;

import com.example.demo.Model.UserAccount;
import com.example.demo.Service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;

    // קבלת כל המשתמשים
    @GetMapping
    public List<UserAccount> getAllUsers() {
        return userAccountService.getAllUsers();
    }

    // קבלת משתמש לפי ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userAccountService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserAccount userAccount) {

        return userAccountService.createUser(userAccount);
    }
    // עדכון משתמש לפי ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserAccount updatedUser) {
        return userAccountService.updateUser(id, updatedUser);
    }

    // מחיקת משתמש לפי ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userAccountService.deleteUser(id);
    }
}
