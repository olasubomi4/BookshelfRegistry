package com.group5.bookshelfregistry.annotations;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileExtensionValidator.class)
@Documented
public @interface AllowedFileExtension {
    String[] value();
    String message() default "Invalid file extension. Allowed extensions are {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
