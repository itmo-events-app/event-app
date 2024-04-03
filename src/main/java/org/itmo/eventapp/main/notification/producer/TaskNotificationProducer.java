package org.itmo.eventapp.main.notification.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.model.dto.TaskNotificationDTO;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TaskNotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final String EXCHANGE_NAME = "notificationExchange";

    @Async
    public void sendTaskNotification(Task task){
        TaskNotificationDTO message = TaskMapper.taskToTaskNotificationDTO(task);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", message);
    }



}
