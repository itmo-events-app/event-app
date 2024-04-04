//package org.itmo.eventApp.main.controller;
//
//import com.icegreen.greenmail.configuration.GreenMailConfiguration;
//import com.icegreen.greenmail.junit5.GreenMailExtension;
//import com.icegreen.greenmail.util.ServerSetupTest;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.itmo.eventapp.main.mail.MailSenderService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.util.FileCopyUtils;
//import org.testcontainers.shaded.org.awaitility.Awaitility;
//
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.nio.charset.StandardCharsets;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class EmailSendingTest extends AbstractTestContainers{
//    @Autowired
//    private MailSenderService mailSenderService;
//
//    @RegisterExtension
//    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
//            .withConfiguration(GreenMailConfiguration.aConfig().withUser("sender@test", "passwd"))
//            .withPerMethodLifecycle(true);
//
//    @Test
//    void testIncomingTaskMessageSending() throws IOException, MessagingException {
//        String expectedUserEmail = "user@test";
//        String expectedUserName = "Tester";
//        String expectedEventName = "TestEvent";
//        String expectedTaskName = "TestTask";
//        String expectedTaskLink = "Link";
//        String expectedSenderEmail = "sender@test";
//        String expectedSubject = "Новая задача!";
//        String expectedMessage = readMessage("email-templates/incoming-task-filled.html");
//
//        mailSenderService.sendIncomingTaskMessage(expectedUserEmail, expectedUserName,
//                expectedEventName, expectedTaskName, expectedTaskLink);
//
//        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length == 1);
//
//        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
//        assertEquals(1, receivedMessage.getAllRecipients().length);
//        assertEquals(expectedUserEmail, receivedMessage.getAllRecipients()[0].toString());
//        assertEquals(expectedSenderEmail, receivedMessage.getFrom()[0].toString());
//        assertEquals(expectedSubject, receivedMessage.getSubject());
//        // replace \r\n over \n to resolve test conflicts on Windows and Linux
//        assertEquals(expectedMessage.replace("\r\n", "\n"), receivedMessage.getContent().toString().replace("\r\n", "\n"));
//    }
//
//    @Test
//    void testOverdueTaskMessageSending() throws IOException, MessagingException {
//        String expectedUserEmail = "user@test";
//        String expectedUserName = "Tester";
//        String expectedEventName = "TestEvent";
//        String expectedTaskName = "TestTask";
//        String expectedTaskLink = "Link";
//        String expectedSenderEmail = "sender@test";
//        String expectedSubject = "Просроченная задача!";
//        String expectedMessage = readMessage("email-templates/overdue-task-filled.html");
//
//        mailSenderService.sendOverdueTaskMessage(expectedUserEmail, expectedUserName,
//                    expectedEventName, expectedTaskName, expectedTaskLink);
//
//        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length == 1);
//
//        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
//        assertEquals(1, receivedMessage.getAllRecipients().length);
//        assertEquals(expectedUserEmail, receivedMessage.getAllRecipients()[0].toString());
//        assertEquals(expectedSenderEmail, receivedMessage.getFrom()[0].toString());
//        assertEquals(expectedSubject, receivedMessage.getSubject());
//        // replace \r\n over \n to resolve test conflicts on Windows and Linux
//        assertEquals(expectedMessage.replace("\r\n", "\n"), receivedMessage.getContent().toString().replace("\r\n", "\n"));
//    }
//
//    @Test
//    void testReminderTaskMessageSending() throws IOException, MessagingException {
//        String expectedUserEmail = "user@test";
//        String expectedUserName = "Tester";
//        String expectedEventName = "TestEvent";
//        String expectedTaskName = "TestTask";
//        String expectedTaskLink = "Link";
//        String expectedSenderEmail = "sender@test";
//        String expectedSubject = "Не забудьте выполнить задачу!";
//        String expectedMessage = readMessage("email-templates/reminder-task-filled.html");
//
//        mailSenderService.sendReminderTaskMessage(expectedUserEmail, expectedUserName,
//                    expectedEventName, expectedTaskName, expectedTaskLink);
//
//        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length == 1);
//
//        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
//        assertEquals(1, receivedMessage.getAllRecipients().length);
//        assertEquals(expectedUserEmail, receivedMessage.getAllRecipients()[0].toString());
//        assertEquals(expectedSenderEmail, receivedMessage.getFrom()[0].toString());
//        assertEquals(expectedSubject, receivedMessage.getSubject());
//        // replace \r\n over \n to resolve test conflicts on Windows and Linux
//        assertEquals(expectedMessage.replace("\r\n", "\n"), receivedMessage.getContent().toString().replace("\r\n", "\n"));
//    }
//
//    //Читает файл шаблона и преобразует в String
//    private String readMessage(String templatePath) throws IOException {
//        Resource resource = new ClassPathResource(templatePath);
//        Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
//        return FileCopyUtils.copyToString(reader);
//    }
//
//}
