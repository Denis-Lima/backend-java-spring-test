package com.selaz.todoapp.utils.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    String message() default "Field should be null or not blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
