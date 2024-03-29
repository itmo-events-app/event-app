package org.itmo.eventapp.main.mail;

import org.springframework.scheduling.annotation.Async;

/**
 * Сервис для отправки писем по электронной почте
 */
public interface MailSenderService {

    /**
     * Отправляет писмо на указанный email с уведомлением о новой задаче
     * Использует шаблон письма о создании задачи
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     * @param taskLink
     */
    void sendIncomingTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink);

    /**
     * Отправляет писмо на указанный email с уведомлением о просроченной задаче
     * Использует шаблон письма о просроченной задачи
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     * @param taskLink
     */
    void sendOverdueTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink);

    /**
     * Отправляет писмо на указанный email с напоминанием в выполнении задачи
     * Использует шаблон письма с напоминанием о задаче
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     * @param taskLink
     */
    void sendReminderTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink);

}
