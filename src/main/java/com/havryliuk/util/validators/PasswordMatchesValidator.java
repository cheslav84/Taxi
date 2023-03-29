package com.havryliuk.util.validators;

import com.havryliuk.dto.UserDTO;
import com.havryliuk.util.validators.annotations.PasswordMatches;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, UserDTO> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserDTO user, ConstraintValidatorContext context) {
        return user.getPassword().equals(user.getMatchingPassword());
    }

}
