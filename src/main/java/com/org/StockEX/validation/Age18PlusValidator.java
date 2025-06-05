package com.org.StockEX.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class Age18PlusValidator implements ConstraintValidator<Age18Plus, LocalDate> {

    @Override
    public boolean isValid(LocalDate dob, ConstraintValidatorContext context) {
        if (dob == null) {
            return false;
        }

        return Period.between(dob, LocalDate.now()).getYears() >= 18;
    }
}
