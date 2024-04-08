package org.itmo.eventapp.main.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.itmo.eventapp.main.model.validation.validator.ValidLoginValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidLoginValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLogin {
    String message() default "Недопустимый логин";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
