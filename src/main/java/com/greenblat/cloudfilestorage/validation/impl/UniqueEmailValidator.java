package com.greenblat.cloudfilestorage.validation.impl;

import com.greenblat.cloudfilestorage.repository.UserRepository;
import com.greenblat.cloudfilestorage.validation.UniqueEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    private final UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByEmail(email);
    }
}
