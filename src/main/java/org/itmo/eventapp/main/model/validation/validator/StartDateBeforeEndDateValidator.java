package org.itmo.eventapp.main.model.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.itmo.eventapp.main.model.validation.annotation.StartDateBeforeEndDate;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

public class StartDateBeforeEndDateValidator implements ConstraintValidator<StartDateBeforeEndDate, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final StartDateBeforeEndDate constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final LocalDateTime firstDate = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
            final LocalDateTime secondDate = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(secondFieldName);

            if ((firstDate == null && secondDate != null) || (firstDate != null && secondDate == null)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Both dates should present or be null");
            }
            return (firstDate == null && secondDate == null) || firstDate.isBefore(secondDate);

        } catch (final Exception ignore) {
        }

        return true;
    }
}