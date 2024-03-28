package org.itmo.eventapp.main.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * Сервис для отправки писем по электронной почте
 */
@Component
public class MailSenderService {
    /*
    TODO: сделать HTML темплейты для писем
    TODO: сделать все нужные методы
     */

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderName;

    /**
     * Метод отправляет писмо пользователю с уведомлением о новой задаче
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     */
    @Async
    public void sendCreatedTaskMessage(String userEmail, String userName, String eventName, String taskName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(userEmail);
            //обязательно нужно указать sender для mail.ru
            message.setFrom(senderName);
            message.setSubject("Новая задача!");
            message.setText("Дорогой пользователь " + userName + "!\n"
                    + "Вам на выполнение поступила новая задача - \"" + taskName + "\" "
                    + "в мероприятии \"" + eventName + "\", не забудте выполнить её в срок!");
            mailSender.send(message);
        } catch (MailException e) {
            //логирование
            e.printStackTrace();
        }
    }
}
