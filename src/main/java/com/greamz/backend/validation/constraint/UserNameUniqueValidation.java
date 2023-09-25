package com.greamz.backend.validation.constraint;





import com.greamz.backend.repository.IAccountRepository;
import com.greamz.backend.validation.annotations.UsernameUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNameUniqueValidation implements ConstraintValidator<UsernameUnique, String> {
    @Autowired
    IAccountRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return !userRepository.existsByUsername(email);
    }
}
