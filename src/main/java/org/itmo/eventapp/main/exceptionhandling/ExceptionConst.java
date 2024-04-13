package org.itmo.eventapp.main.exceptionhandling;

public class ExceptionConst {
    public static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";
    public static final String EVENT_PARENT_NOT_FOUND_MESSAGE = "Event's parent not found";
    public static final String EVENT_ROLE_NOT_FOUND_MESSAGE = "EventRole not found";
    public static final String ACTIVITY_RECURSION = "Cannot create activity for another activity";
    public static final String PLACE_NOT_FOUND_MESSAGE = "Place not found";

    public static final String ROLE_ID_NOT_FOUND_MESSAGE = "Роль с id %d не найдена";
    public static final String ROLE_NAME_NOT_FOUND_MESSAGE = "Роль с именем %s не найдена";
    public static final String ROLE_EXIST_MESSAGE = "Роль с таким именем уже существует";
    public static final String ROLE_EDITING_FORBIDDEN_MESSAGE = "Невозможно изменить эту роль";
    public static final String ROLE_DELETING_FORBIDDEN_MESSAGE = "Невозможно удалить эту роль";
    public static final String INVALID_ROLE_TYPE = "Неверный тип роли: ожидалась %s роль";
    public static final String ROLE_ASSIGNMENT_FORBIDDEN_MESSAGE = "Нельзя назначить эту роль";
    public static final String ROLE_REVOKING_FORBIDDEN_MESSAGE = "Нельзя лишить этой роли";
    public static final String USERS_WITH_ROLE_EXIST = "Невозможно удалить роль, так как существуют пользователи, которым она назначена";
    public static final String REVOKE_SELF_ROLE_FORBIDDEN_MESSAGE = "Невозможно лишить роли себя";
    public static final String ASSIGN_SELF_ROLE_FORBIDDEN_MESSAGE = "Невозможно назначить роль себе";

    public static final String PRIVILEGE_ID_NOT_FOUND_MESSAGE = "Привилегия с id %d не найдена";

    public static final String USER_ROLE_NOT_FOUND_IN_EVENT_MESSAGE = "У пользователя с id %d нет роли %s в мероприятии с id %d";
    public static final String AT_LEAST_ONE_ORGANIZER_MESSAGE = "Мероприятие должно содержать не менее одного пользователя с ролью Организатор";

    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
    public static final String USER_EMAIL_EXIST = "User with this email exists";
    public static final String USER_PASSWORD_MISMATCH = "Password mismatch";

    public static final String NOTIFICATION_ERROR_MESSAGE = "Notification fetching error, please refresh";

    public static final String TASK_NOT_FOUND_MESSAGE = "Task not found";

    public static final String REGISTRATION_REQUEST_NOT_FOUND_MESSAGE = "Registration request not found";
    public static final String REGISTRATION_REQUEST_EMAIL_EXIST = "Registration request with this email exists";

    public static final String REGISTRATION_REQUEST_NOT_APPROVED = "Registration request not approved";

    public static final String INVALID_TYPE = "Invalid type";

    public static final String EMAIL_NOT_APPROVED = "Невозможно восстановить пароль без подтвержденной почты";


    private ExceptionConst() {
    }
}
