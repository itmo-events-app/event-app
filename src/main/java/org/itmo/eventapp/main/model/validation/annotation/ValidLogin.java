package org.itmo.eventapp.main.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.validation.validator.PasswordMatchingValidator;
import org.itmo.eventapp.main.model.validation.validator.ValidLoginValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = ValidLoginValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLogin {
    String login() default "login";
    LoginType type() default LoginType.EMAIL;
    String message() default "Недопустимый логин";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
