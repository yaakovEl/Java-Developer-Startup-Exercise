package com.example.demo.repository;

import com.example.demo.model.userAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
//Search by email
public interface UserAccountRepository extends JpaRepository<userAccount, Long> {
    userAccount findByEmail(String email);
}
