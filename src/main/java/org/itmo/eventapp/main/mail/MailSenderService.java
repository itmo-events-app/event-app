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

}
