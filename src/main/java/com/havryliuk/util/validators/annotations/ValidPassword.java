package com.havryliuk.util.validators.annotations;

import com.havryliuk.util.validators.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
    String message() default
            "Should be minimum eight characters, at least one letter, one number and one special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}