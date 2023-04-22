package com.rader.algafood.core.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;


public class FileContentTypeValidation implements ConstraintValidator<FileContentType, MultipartFile> {

    private List<String> allowedContentTypes;


    @Override
    public void initialize(FileContentType constraint) {
        this.allowedContentTypes = Arrays.asList(constraint.allowed());
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        return multipartFile == null
                || this.allowedContentTypes.contains(multipartFile.getContentType());
    }

}

