package org.itmo.eventapp.main.mail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация сервиса для отправки писем по электронной почте
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String senderName;

    @Async
    @Override
    public void sendIncomingTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) throws MessagingException, IOException {
        String subject = "Новая задача!";
        String templatePath = "notification/email-templates/incoming-task-template.html";
        mailSender.send(createMessageFromTemplate(userEmail, subject, templatePath, getTaskNotificationTemplateFields(userName, eventName, taskName, taskLink)));
    }

    @Async
    @Override
    public void sendOverdueTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) throws MessagingException, IOException {
        String subject = "Просроченная задача!";
        String templatePath = "notification/email-templates/overdue-task-template.html";
        mailSender.send(createMessageFromTemplate(userEmail, subject, templatePath, getTaskNotificationTemplateFields(userName, eventName, taskName, taskLink)));
    }

    @Async
    @Override
    public void sendReminderTaskMessage(String userEmail, String userName, String eventName, String taskName, String taskLink) throws MessagingException, IOException {
        String subject = "Не забудьте выполнить задачу!";
        String templatePath = "notification/email-templates/reminder-task-template.html";
        mailSender.send(createMessageFromTemplate(userEmail, subject, templatePath, getTaskNotificationTemplateFields(userName, eventName, taskName, taskLink)));
    }

    @Async
    @Override
    public void sendApproveRegistrationRequestMessage(String userEmail, String userName) throws MessagingException, IOException {
        String subject = "Заявка на регистрацию одобрена";
        String templatePath = "notification/email-templates/approve-registration-request.html";
        mailSender.send(createMessageFromTemplate(userEmail, subject, templatePath, getRegistrationResponseTemplateFields(userName)));
    }

    @Async
    @Override
    public void sendDeclineRegistrationRequestMessage(String userEmail, String userName) throws MessagingException, IOException {
        String subject = "Заявка на регистрацию отклонена";
        String templatePath = "notification/email-templates/decline-registration-request.html";
        mailSender.send(createMessageFromTemplate(userEmail, subject, templatePath, getRegistrationResponseTemplateFields(userName)));
    }

    @Async
    @Override
    public void sendRecoveryPasswordMessage(String userEmail, String userName, String url) throws MessagingException, IOException {
        String subject = "Восстановление пароля";
        String templatePath = "notification/email-templates/recovery-password.html";
        mailSender.send(createMessageFromTemplate(userEmail, subject, templatePath, getRecoveryPasswordTemplateFields(userName, url)));

    }

    // Создаёт MIME письмо для отправки
    private MimeMessage createMessageFromTemplate(String recipient, String subject, String templatePath, Map<String, String> templateFields) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(senderName);
        message.setRecipients(Message.RecipientType.TO, recipient);
        message.setSubject(subject);
        String messageContent = readTemplate(templatePath);
        for (Map.Entry<String, String> templateField : templateFields.entrySet()) {
            messageContent = messageContent.replace(templateField.getKey(), templateField.getValue());
        }
        message.setContent(messageContent, "text/html; charset=utf-8");
        return message;
    }

    // Сопоставляет пары ключ:значение для шаблонов писам, свзанных с задаами
    private Map<String, String> getTaskNotificationTemplateFields(String userName, String eventName, String taskName, String taskLink) {
        HashMap<String, String> templateFields = new HashMap<>();
        templateFields.put("${userName}", userName);
        templateFields.put("${eventName}", eventName);
        templateFields.put("${taskName}", taskName);
        templateFields.put("${taskLink}", taskLink);
        return templateFields;
    }

    private Map<String, String> getRegistrationResponseTemplateFields(String userName) {
        HashMap<String, String> templateFields = new HashMap<>();
        templateFields.put("${userName}", userName);
        return templateFields;
    }

    private Map<String, String> getRecoveryPasswordTemplateFields(String userName, String url) {
        HashMap<String, String> templateFields = new HashMap<>();
        templateFields.put("${userName}", userName);
        templateFields.put("${url}", url);
        return templateFields;
    }

    //Читает файл шаблона и преобразует в String
    private String readTemplate(String templatePath) throws IOException {
        Resource resource = new ClassPathResource(templatePath);
        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
        return FileCopyUtils.copyToString(reader);
    }
}
