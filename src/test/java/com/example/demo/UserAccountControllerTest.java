package com.example.demo;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import com.example.demo.model.userAccount;
import com.example.demo.service.UserAccountService;
import com.example.demo.controller.UserAccountController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class UserAccountControllerTest {
    @Mock
    private UserAccountService userAccountService;

    @InjectMocks
    private UserAccountController userAccountController;

    @Test
    public void testGetUserById() {
        // Initialize Mocks
        MockitoAnnotations.openMocks(this);
        // Mock data
        userAccount userAccount = new userAccount();
        userAccount.setId(1L);
        userAccount.setEmail("test@example.com");
        // Mock response from the service
        ResponseEntity<?> mockResponse = ResponseEntity.ok(userAccount);
        when(userAccountService.getUserById(eq(1L))).thenReturn((ResponseEntity) mockResponse);
        // Call the controller method
        ResponseEntity<?> result = userAccountController.getUserById(1L);
        // Verify the result
        assertThat(result).isNotNull();
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
        assertThat(result.getBody()).isEqualTo(userAccount);
        // Verify the service interaction
        verify(userAccountService, times(1)).getUserById(1L);
    }
}
