package com.greenblat.cloudfilestorage.integration.service;

import com.greenblat.cloudfilestorage.dto.AuthRequest;
import com.greenblat.cloudfilestorage.exception.ResourceAlreadyExistsException;
import com.greenblat.cloudfilestorage.integration.IntegrationTestBase;
import com.greenblat.cloudfilestorage.repository.UserRepository;
import com.greenblat.cloudfilestorage.service.AuthService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthServiceIT extends IntegrationTestBase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthService authService;

    @Autowired
    AuthServiceIT(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @AfterEach
    public void clearTable() {
        userRepository.deleteAll();
    }

    @Test
    void saveUser() {
        var expected = new AuthRequest(
                "username",
                "password",
                "fifaj@mail,ru",
                "89111234567"
        );
        authService.saveUser(expected);

        var actual = userRepository.findByUsername(expected.username());

        assertThat(actual.isPresent()).isTrue();
        assertThat(actual.get().getUsername()).isEqualTo(expected.username());
        assertThat(actual.get().getEmail()).isEqualTo(expected.email());
        assertThat(actual.get().getPhoneNumber()).isEqualTo(expected.phoneNumber());
        assertThat(passwordEncoder.matches(expected.password(), actual.get().getPassword())).isTrue();
    }

    @Test
    void thrownExceptionIfUsernameExists() {
        var user1 = new AuthRequest(
                "username",
                "password",
                "fifaj@mail,ru",
                "89111234567"
        );
        authService.saveUser(user1);

        var user2 = new AuthRequest(
                "username",
                "password123",
                "sfjoerjgopjre@mail,ru",
                "89121311234567"
        );
        assertThatThrownBy(() -> authService.saveUser(user2))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessage(String.format("user with username [%s] exists", user2.username()));
    }
}