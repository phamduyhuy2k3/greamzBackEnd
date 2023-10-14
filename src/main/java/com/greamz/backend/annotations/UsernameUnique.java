package com.greamz.backend.annotations;

import com.greamz.backend.validation.constraint.UserNameUniqueValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserNameUniqueValidation.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameUnique {
    String message() default "Username này đã tồn tại";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
