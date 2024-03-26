package com.group5.bookshelfregistry.annotations;

import com.group5.bookshelfregistry.enums.Roles;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.stream.Stream;

public class RoleValidator implements ConstraintValidator<Role,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return true;
        }

        return Stream.of(Roles.values())
                .map(a -> a.getRoleName())
                .anyMatch(name -> name.equalsIgnoreCase(value));    }
}
