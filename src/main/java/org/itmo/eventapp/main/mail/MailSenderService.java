package org.itmo.eventapp.main.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Сервис для отправки писем по электронной почте
 */
@Component
public class MailSenderService {
    //todo добавить логирование

    @Autowired
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderName;

    /**
     * Отправляет писмо на указанный email с уведомлением о новой задаче
     * Использует шаблон письма о создании задачи
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     * @param taskLink
     */
    @Async
    public void sendIncomingTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(senderName);
            message.setRecipients(MimeMessage.RecipientType.TO, userEmail);
            message.setSubject("Новая задача!");
            String messageContent = readTemplate("classpath:email-templates/incoming-task-template")
                    .replace("${userName}", userName)
                    .replace("${eventName}", eventName)
                    .replace("${taskName}", taskName)
                    .replace("${taskLink}", taskLink);
            message.setContent(messageContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            //логирование
            e.printStackTrace();
        } catch (IOException e) {
            //логирование
            e.printStackTrace();
        }
    }

    /**
     * Отправляет писмо на указанный email с уведомлением о просроченной задаче
     * Использует шаблон письма о просроченной задачи
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     * @param taskLink
     */
    @Async
    public void sendOverdueTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(senderName);
            message.setRecipients(MimeMessage.RecipientType.TO, userEmail);
            message.setSubject("Просроченная задача!");
            String messageContent = readTemplate("classpath:email-templates/overdue-task-template")
                    .replace("${userName}", userName)
                    .replace("${eventName}", eventName)
                    .replace("${taskName}", taskName)
                    .replace("${taskLink}", taskLink);
            message.setContent(messageContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            //логирование
            e.printStackTrace();
        } catch (IOException e) {
            //логирование
            e.printStackTrace();
        }
    }

    /**
     * Отправляет писмо на указанный email с напоминанием в выполнении задачи
     * Использует шаблон письма с напоминанием о задаче
     * @param userEmail
     * @param userName
     * @param eventName
     * @param taskName
     * @param taskLink
     */
    @Async
    public void sendReminderTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(senderName);
            message.setRecipients(MimeMessage.RecipientType.TO, userEmail);
            message.setSubject("Не забудьте выполнить задачу!");
            String messageContent = readTemplate("classpath:email-templates/reminder-task-template")
                    .replace("${userName}", userName)
                    .replace("${eventName}", eventName)
                    .replace("${taskName}", taskName)
                    .replace("${taskLink}", taskLink);
            message.setContent(messageContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            //логирование
            e.printStackTrace();
        } catch (IOException e) {
            //логирование
            e.printStackTrace();
        }
    }

    //Читает файл шаблона и преобразует в String
    private String readTemplate(String templatePath) throws IOException {
        Resource resource = new ClassPathResource(templatePath);
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}
