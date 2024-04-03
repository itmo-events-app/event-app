package org.itmo.eventapp.main.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue emailNotificationQueue() {
        return new Queue("notification.email", false);
    }

    @Bean
    public Queue inAppNotificationQueue() {
        return new Queue("notification.inApp", false);
    }

    @Bean
    public FanoutExchange notificationExchange() {
        return new FanoutExchange("notificationExchange");
    }

    @Bean
    public Binding emailFanoutBinding() {
        return BindingBuilder.bind(emailNotificationQueue()).to(notificationExchange());
    }

    @Bean
    public Binding inAppFanoutBinding() {
        return BindingBuilder.bind(inAppNotificationQueue()).to(notificationExchange());
    }

    @Bean
    public SimpleMessageConverter converter() {
        SimpleMessageConverter converter = new SimpleMessageConverter();
        converter.setAllowedListPatterns(List.of("org.itmo.eventapp.main.model.dto.TaskNotificationDTO"));
        return converter;
    }

}
