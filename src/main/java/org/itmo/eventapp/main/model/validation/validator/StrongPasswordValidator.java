package org.itmo.eventapp.main.model.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.itmo.eventapp.main.model.validation.annotation.StrongPassword;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    private boolean isAtLeastOneUpperAndLowerCase(String s) {
        boolean upper = false;
        boolean lower = false;

        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) upper = true;
            if (Character.isLowerCase(c)) lower = true;
        }
        return upper && lower;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        Pattern pattern = Pattern.compile("[!\"#$%&'()*+,\\-./:;<=>?@\\[\\\\\\]^_`{|}~]+");
        Matcher matcher = pattern.matcher(s);

        return isAtLeastOneUpperAndLowerCase(s) && s.length() >= 8 && matcher.find();
    }
}
