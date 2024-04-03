package org.itmo.eventapp.main.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.itmo.eventapp.main.model.dto.TaskNotificationDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "notification.email")
public class EmailNotificationConsumer {

    @RabbitHandler
    public void receive(TaskNotificationDTO taskInfo){
        //TODO implement logic
        System.out.println("Message content for email is: " + taskInfo.taskName()
                + ", " + taskInfo.taskAssignerEmail() + ", "
                + taskInfo.taskAssignerName() + ", "
                + taskInfo.taskAssigneeEmail() + ", "
                + taskInfo.taskAssigneeName() + ", "
                + taskInfo.taskEventName() + ", "
                + taskInfo.taskStatus());
    }
}
