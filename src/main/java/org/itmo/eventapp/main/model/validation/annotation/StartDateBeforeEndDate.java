package org.itmo.eventapp.main.model.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.itmo.eventapp.main.model.validation.validator.StartDateBeforeEndDateValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Validation annotation to validate that first LocalDateTime field is before second LocalDateTime field or both null.
 * <p>
 * An array of fields and their matching confirmation fields can be supplied.
 * <p>
 * Example, compare 1 pair of fields:
 *
 * @StartDateBeforeEndDate(first = "startDate", second = "endDate", message = "startDate should be before endDate")
 * <p>
 * Example, compare more than 1 pair of fields:
 * @StartDateBeforeEndDate.List({
 * @StartDateBeforeEndDate(first = "startDate", second = "endDate", message = "startDate should be before endDate"),
 * @StartDateBeforeEndDate(first = "firstDate", second = "secondDate", message = "firstDate should be before secondDate")})
 */
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = StartDateBeforeEndDateValidator.class)
@Documented
public @interface StartDateBeforeEndDate {
    String message() default "Dates validation error. First date should be before second date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String first();

    /**
     * @return The second field
     */
    String second();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see StartDateBeforeEndDate
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        StartDateBeforeEndDate[] value();
    }
}