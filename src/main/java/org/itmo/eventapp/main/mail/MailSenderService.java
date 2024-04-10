package org.itmo.eventapp.main.mail;

import jakarta.mail.MessagingException;

import java.io.IOException;

/**
 * Сервис для отправки писем по электронной почте
 */
public interface MailSenderService {

    /**
     * Отправляет писмо на указанный email с уведомлением о новой задаче
     * Использует шаблон письма о создании задачи
     * @param userEmail - почта, на которую отправится письмо
     * @param userName - имя пользователя для шаблона
     * @param eventName - название мероприятия для шаблона
     * @param taskName - название задачи для шаблона
     * @param taskLink - ссылка на задачу
     */
    void sendIncomingTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) throws MessagingException, IOException;

    /**
     * Отправляет писмо на указанный email с уведомлением о просроченной задаче
     * Использует шаблон письма о просроченной задачи
     * @param userEmail - почта, на которую отправится письмо
     * @param userName - имя пользователя для шаблона
     * @param eventName - название мероприятия для шаблона
     * @param taskName - название задачи для шаблона
     * @param taskLink - ссылка на задачу
     */
    void sendOverdueTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) throws MessagingException, IOException;

    /**
     * Отправляет писмо на указанный email с напоминанием в выполнении задачи
     * Использует шаблон письма с напоминанием о задаче
     * @param userEmail - почта, на которую отправится письмо
     * @param userName - имя пользователя для шаблона
     * @param eventName - название мероприятия для шаблона
     * @param taskName - название задачи для шаблона
     * @param taskLink - ссылка на задачу
     */
    void sendReminderTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) throws MessagingException, IOException;

    /**
     * Отправляет писмо на указанный email с уведомлением о принятии заявки на регистрацию
     * Использует шаблон письма о создании задачи
     * @param userEmail - почта, на которую отправится письмо
     * @param userName - имя пользователя для шаблона
     */
    void sendApproveRegistrationRequestMessage(String userEmail, String userName) throws MessagingException, IOException;

    /**
     * Отправляет писмо на указанный email с уведомлением об отказе в регистрации
     * Использует шаблон письма о создании задачи
     * @param userEmail - почта, на которую отправится письмо
     * @param userName - имя пользователя для шаблона
     */
    void sendDeclineRegistrationRequestMessage(String userEmail, String userName) throws MessagingException, IOException;

    /**
     * Отправляет письмо на указанный email с ссылкой на восстановление пароля
     * @param userEmail - почта, на которую отправится письмо
     * @param userName - имя пользователя для шаблона
     * @param url - адрес метода установки нового пароля
     */
    void sendRecoveryPasswordMessage(String userEmail, String userName, String url) throws MessagingException, IOException;
}
