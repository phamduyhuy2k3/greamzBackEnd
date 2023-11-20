package com.greamz.backend.validation.constraint;

import com.greamz.backend.repository.IAccountRepo;
import com.greamz.backend.validation.annotations.UsernameUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserNameUniqueValidation implements ConstraintValidator<UsernameUnique, String> {
    @Autowired
    IAccountRepo userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(email);
    }
}
