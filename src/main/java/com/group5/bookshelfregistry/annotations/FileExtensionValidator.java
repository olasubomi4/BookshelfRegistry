package com.group5.bookshelfregistry.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

public class FileExtensionValidator implements ConstraintValidator<AllowedFileExtension, MultipartFile> {

    private String[] allowedExtensions;

    @Override
    public void initialize(AllowedFileExtension constraintAnnotation) {
        this.allowedExtensions = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
//        return Arrays.asList(allowedExtensions).contains(fileExtension);
        return false;
    }
}
