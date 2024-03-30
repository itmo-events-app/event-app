package org.itmo.eventapp.main.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/**
 * Реализация сервиса для отправки писем по электронной почте
 */
@RequiredArgsConstructor
@Service
public class MailSenderServiceImpl implements MailSenderService{
    //todo добавить логирование

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderName;

    @Async
    @Override
    public void sendIncomingTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(senderName);
            message.setRecipients(Message.RecipientType.TO, userEmail);
            message.setSubject("Новая задача!");
            String messageContent = readTemplate("notification/email-templates/incoming-task-template.html")
                    .replace("${userName}", userName)
                    .replace("${eventName}", eventName)
                    .replace("${taskName}", taskName)
                    .replace("${taskLink}", taskLink);
            message.setContent(messageContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            //логирование
        } catch (IOException e) {
            //логирование
        }
    }

    @Async
    @Override
    public void sendOverdueTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(senderName);
            message.setRecipients(Message.RecipientType.TO, userEmail);
            message.setSubject("Просроченная задача!");
            String messageContent = readTemplate("notification/email-templates/overdue-task-template.html")
                    .replace("${userName}", userName)
                    .replace("${eventName}", eventName)
                    .replace("${taskName}", taskName)
                    .replace("${taskLink}", taskLink);
            message.setContent(messageContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            //логирование
        } catch (IOException e) {
            //логирование
        }
    }

    @Async
    @Override
    public void sendReminderTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            message.setFrom(senderName);
            message.setRecipients(Message.RecipientType.TO, userEmail);
            message.setSubject("Не забудьте выполнить задачу!");
            String messageContent = readTemplate("notification/email-templates/reminder-task-template.html")
                    .replace("${userName}", userName)
                    .replace("${eventName}", eventName)
                    .replace("${taskName}", taskName)
                    .replace("${taskLink}", taskLink);
            message.setContent(messageContent, "text/html; charset=utf-8");
            mailSender.send(message);
        } catch (MailException | MessagingException e) {
            //логирование
        } catch (IOException e) {
            //логирование
        }
    }

    //Читает файл шаблона и преобразует в String
    private String readTemplate(String templatePath) throws IOException {
        Resource resource = new ClassPathResource(templatePath);
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}
