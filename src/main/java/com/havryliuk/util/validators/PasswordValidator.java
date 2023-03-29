package com.havryliuk.util.validators;

import com.havryliuk.util.validators.annotations.ValidPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

 //Minimum eight characters, at least one letter, one number and one special character:
    private static final String PASSWORD_PATTERN
            = "^(?=.*[A-Za-z])(?=.*\\\\d)(?=.*[@$!%*#?&])[A-Za-z\\\\d@$!%*#?&]{8,32}$";

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return (validatePassword(password));
    }

    private boolean validatePassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
