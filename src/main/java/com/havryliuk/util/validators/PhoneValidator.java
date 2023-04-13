package com.havryliuk.util.validators;

import com.havryliuk.util.validators.annotations.ValidPhone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_PATTERN = "^(\\+[1-9]{1}[0-9]{11})|([0]{1}[0-9]{9})$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return (validatePhone(phone));
    }

    private boolean validatePhone(String phone) {
        phone = phone.replaceAll("[\\s()-]", "");
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
