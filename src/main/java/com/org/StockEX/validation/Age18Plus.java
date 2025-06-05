package com.org.StockEX.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = Age18PlusValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Age18Plus {
    String message() default "Users must be above 18 or above.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

