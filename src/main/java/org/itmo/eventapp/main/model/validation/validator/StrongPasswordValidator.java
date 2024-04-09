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

        if(s.length() <= 8){
            constraintValidatorContext.disableDefaultConstraintViolation(); // Отключаем стандартное сообщение
            constraintValidatorContext.buildConstraintViolationWithTemplate("Минимальная длина пароля - 8 символов").addConstraintViolation();

            return false;
        }

        if (!isAtLeastOneUpperAndLowerCase(s)){
            constraintValidatorContext.disableDefaultConstraintViolation(); // Отключаем стандартное сообщение
            constraintValidatorContext.buildConstraintViolationWithTemplate("Пароль должен содержать минимум один символ верхнего и нижнего регистра").addConstraintViolation();

            return false;
        }

        if(!matcher.find()){
            constraintValidatorContext.disableDefaultConstraintViolation(); // Отключаем стандартное сообщение
            constraintValidatorContext.buildConstraintViolationWithTemplate("Пароль должен содержать минимум один специальный символ").addConstraintViolation();

            return false;
        }

        return true;
    }
}
