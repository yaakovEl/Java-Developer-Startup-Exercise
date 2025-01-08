package com.example.demo.Repository;

import com.example.demo.Model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // לא חובה בגרסאות מודרניות של Spring Boot, אבל מוסיף קריאות
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    // ניתן להוסיף שאילתות מותאמות אישית כאן אם צריך
    UserAccount findByEmail(String email); // דוגמה לשאילתה מותאמת
}
