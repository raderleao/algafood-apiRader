package com.rader.algafood.core.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = { FileContentTypeValidation.class })
public @interface FileContentType {

    String message() default "arquivo inválido";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String[] allowed();

}

