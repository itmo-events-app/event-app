package org.itmo.eventapp.main.model.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.itmo.eventapp.main.model.entity.enums.LoginType;
import org.itmo.eventapp.main.model.validation.annotation.ValidLogin;
import org.springframework.beans.BeanWrapperImpl;

public class ValidLoginValidator implements ConstraintValidator<ValidLogin, Object> {

    //Обязательные поля, которые должны присутствовать в этих клаасах, где мы валидируем поля
    private String login;
    private String type;

    @Override
    public void initialize(ValidLogin constraintAnnotation) {
        this.login = "login";
        this.type = "type";
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        Object loginValue = new BeanWrapperImpl(o).getPropertyValue(login);
        Object typeValue = new BeanWrapperImpl(o).getPropertyValue(type);


        if (loginValue instanceof String && typeValue instanceof LoginType) {
            // Проверяем, является ли логин действительным адресом электронной почты
            if (typeValue == LoginType.EMAIL && !isValidEmail((String) loginValue)) {
                context.disableDefaultConstraintViolation(); // Отключаем стандартное сообщение
                context.buildConstraintViolationWithTemplate("Некорректный email. Поддерживаемые домены: @itmo.ru, @idu.itmo.ru и @niuitmo.ru").addConstraintViolation();
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        return email.matches("^\\w[\\w\\-.]*@(niu|idu.)?itmo\\.ru$");
    }

}
