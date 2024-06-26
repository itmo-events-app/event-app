package org.itmo.eventapp.main.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.validation.validator.ValidLoginValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Для того, чтобы использовать этот валидатор
 * нужно чтобы у класса обязательно присутствавали
 * поля "login" и "type"
 * Пример: @see org.itmo.eventapp.main.model.dto.request.UserChangeLoginRequest
 */

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
