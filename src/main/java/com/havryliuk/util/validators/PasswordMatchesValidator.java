package com.havryliuk.util.validators;

import com.havryliuk.dto.UserDto;
import com.havryliuk.util.validators.annotations.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDto> {
    private String field;
    private String message;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.field = constraintAnnotation.field();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext context) {
        boolean valid = user.getPassword().equals(user.getMatchingPassword());
        if (!valid){
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(field)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
