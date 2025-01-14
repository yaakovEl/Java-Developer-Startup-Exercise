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

    @GetMapping
    public List<UserAccount> getAllUsers() {
        return userAccountService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userAccountService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserAccount userAccount) {

        return userAccountService.createUser(userAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody UserAccount updatedUser,
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return userAccountService.updateUser(id, updatedUser, jwtToken);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userAccountService.deleteUser(id);
    }
}
