package org.itmo.eventapp.main.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

/**
 * Конфигурация бина почтового сервиса для отправки писем через SMTP сервер Mail.ru
 */
@Configuration
public class MailSenderConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private int connectiontimeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private int writetimeout;

    @Value("${spring.mail.properties.mail.smtp.ssl.enable}")
    private boolean sslEnable;

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.timeout", timeout);
        properties.put("mail.smtp.connectiontimeout", connectiontimeout);
        properties.put("mail.smtp.writetimeout", writetimeout);
        properties.put("mail.smtp.ssl.enable", sslEnable);

        return mailSender;
    }

}
