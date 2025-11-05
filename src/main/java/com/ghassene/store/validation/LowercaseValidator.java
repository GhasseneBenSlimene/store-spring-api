package com.ghassene.store.validation;

public class LowercaseValidator implements jakarta.validation.ConstraintValidator<Lowercase, String> {
    @Override
    public boolean isValid(String value, jakarta.validation.ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.equals(value.toLowerCase());
    }
}
