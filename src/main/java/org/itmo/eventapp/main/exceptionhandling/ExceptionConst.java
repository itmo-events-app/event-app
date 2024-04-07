package org.itmo.eventapp.main.exceptionhandling;

public class ExceptionConst {
    public static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";
    public static final String EVENT_PARENT_NOT_FOUND_MESSAGE = "Event's parent not found";
    public static final String PLACE_NOT_FOUND_MESSAGE = "Place not found";
    public static final String ROLE_NOT_FOUND_MESSAGE = "Role not found";
    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
    public static final String USER_EMAIL_EXIST = "User with this email exists";
    public static final String USER_PASSWORD_MISMATCH = "Password mismatch";
    public static final String NOTIFICATION_ERROR_MESSAGE = "Notification fetching error, please refresh";

    public static final String REGISTRATION_REQUEST_NOT_FOUND_MESSAGE = "Registration request not found";
    public static final String REGISTRATION_REQUEST_EMAIL_EXIST = "Registration request with this email exists";

    public static final String REGISTRATION_REQUEST_NOT_APPROVED = "Registration request not approved";


    private ExceptionConst() {
    }
}
