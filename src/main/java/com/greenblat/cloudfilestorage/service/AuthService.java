package com.greenblat.cloudfilestorage.service;

import com.greenblat.cloudfilestorage.dto.AuthRequest;
import com.greenblat.cloudfilestorage.exception.ResourceAlreadyExistsException;
import com.greenblat.cloudfilestorage.model.User;
import com.greenblat.cloudfilestorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveUser(AuthRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ResourceAlreadyExistsException(String.format("user with username [%s] exists", request.username()));
        }

        var user = new User(
                null,
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.phoneNumber()
        );
        userRepository.save(user);
    }
}
