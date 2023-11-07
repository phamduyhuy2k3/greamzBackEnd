package com.greamz.backend.validation.constraint;


import com.greamz.backend.validation.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;

import java.util.List;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {
    private final PasswordValidator validator;
    @Override
    public void initialize(final ValidPassword arg0) {

    }

    @SneakyThrows
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {


        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);
        String messageTemplate = String.join("\n", messages);
        String[] errors = Pattern.compile("\\.").split(messageTemplate);
        for (String error : errors) {
            context.buildConstraintViolationWithTemplate(error).addConstraintViolation().disableDefaultConstraintViolation();
        }
        return false;
    }
}
