package com.lab.model;

import com.lab.model.config.util.Role;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.UserRepository;
import com.lab.model.service.RoleService;
import com.lab.model.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class AuthTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * This test verifies that the findByName method of RoleService is called with the argument "AUTH"
     * and checks if the user has the "AUTH" role added before saving it
     */
    @Test
    void userIsSavedWithDefaultRole() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");

        RoleEntity authRoleEntity = new RoleEntity();
        authRoleEntity.setRole(Role.AUTH);

        Mockito.when(roleService.findByName(Role.AUTH)).thenReturn(Optional.of(authRoleEntity));

        // Act
        userService.register(userEntity);

        // Assert
        Mockito.verify(roleService, Mockito.times(1)).findByName(Role.AUTH);

        // Ensure that the "AUTH" role is added to the user
        Assertions.assertTrue(userEntity.getRoles().contains(authRoleEntity));

        // Ensure that the user is saved
        Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
    }

    /**
     * This test checks if the BCryptPasswordEncoder.encode method is called and verifies that
     * the user's password is encrypted before saving it.
     */
    @Test
    void userPasswordIsEncrypted() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");

        // Act
        userService.register(userEntity);

        // Ensure that the password is encrypted
        Assertions.assertNotEquals("password", userEntity.getPassword());
        Assertions.assertTrue(new BCryptPasswordEncoder().matches("password", userEntity.getPassword()));

        // Ensure that the user is saved
        Mockito.verify(userRepository, Mockito.times(1)).save(userEntity);
    }
}
