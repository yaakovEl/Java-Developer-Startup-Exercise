package com.example.demo.controller;

import com.example.demo.model.userAccount;
import com.example.demo.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    @Autowired
    private UserAccountService userAccountService;
    //get All users
    @GetMapping
    public List<userAccount> getAllUsers() {
        return userAccountService.getAllUsers();
    }
    //get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userAccountService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody userAccount userAccount) {
        return userAccountService.createUser(userAccount);
    }
    /**
     * Updates an existing user's details based on their ID.
     * The method expects the user ID in the path, updated user details in the request body,
     * and a JWT token in the Authorization header for authentication.
     **/
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody userAccount updatedUser,
            /* Sending a token in the Header */
            @RequestHeader("Authorization") String token) {
        String jwtToken = token.startsWith("Bearer ") ? token.substring(7) : token;
        return userAccountService.updateUser(id, updatedUser, jwtToken);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userAccountService.deleteUser(id);
    }
}
