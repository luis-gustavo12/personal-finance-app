package com.github.Finance.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.github.Finance.models.Role;
import com.github.Finance.models.User;
import com.github.Finance.models.UserStatus;
import com.github.Finance.repositories.UserRepository;
import com.github.Finance.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private Role role;
    private User user;
    private final LocalDateTime localDateTime = LocalDateTime.now();


    @BeforeEach
    void setUp() {

        role = new Role();
        role.setId(1L);
        role.setRole("USER");

        

        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPassword("password123");
        user.setRole(role);
        user.setCreatedAt(localDateTime);
        user.setLastUpdate(localDateTime);
        user.setFirstName("John");
        user.setLastName("Doe");



    }

    @Test
    void testGetUserDetails() {


        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // assertions

        UserDetails result = userService.loadUserByUsername(user.getEmail());

        assertNotNull(result);
        assertEquals("user@example.com", result.getUsername());
        assertEquals("password123", result.getPassword());

        


    }

    @Test
    void testGetUser() {

        when(repository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        // assertions

        assertNotNull(result);

        assertEquals(result.getEmail(), user.getEmail());
        assertEquals(result.getRole().getRole(), user.getRole().getRole());
        assertEquals(result.getFirstName(), user.getFirstName());
        assertEquals(result.getPassword(), user.getPassword());




    }

    
}
