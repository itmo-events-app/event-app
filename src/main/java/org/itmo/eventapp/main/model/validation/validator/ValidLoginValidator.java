package org.itmo.eventapp.main.model.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.itmo.eventapp.main.model.dto.request.CommonLoginRequest;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.validation.annotation.ValidLogin;

import java.util.Objects;

public class ValidLoginValidator implements ConstraintValidator<ValidLogin, CommonLoginRequest> {

    @Override
    public boolean isValid(CommonLoginRequest commonLoginRequest, ConstraintValidatorContext context) {
        String login = commonLoginRequest.login();
        LoginType type = commonLoginRequest.type();

        if (type == LoginType.EMAIL) {// Проверяем, является ли логин действительным адресом электронной почты
            if (!isValidEmail(login)) {
                context.disableDefaultConstraintViolation(); // Отключаем стандартное сообщение
                context.buildConstraintViolationWithTemplate("Некорректный email. Поддерживаемые домены: @itmo.ru, @idu.itmo.ru и @niuitmo.ru").addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^\\w[\\w\\-.]*@(niu|idu.)?itmo\\.ru$");
    }

}
