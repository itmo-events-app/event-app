package org.itmo.eventapp.main.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.itmo.eventapp.main.model.validation.validator.StrongPasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {
    String message() default "Пароль должен содержать 1 символ верхнего и нижнего регистра. Минимальная длина пароля - 8 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
