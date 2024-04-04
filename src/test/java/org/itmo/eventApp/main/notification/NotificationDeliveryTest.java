package org.itmo.eventApp.main.notification;

import org.awaitility.Awaitility;
import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.dto.TaskNotificationDTO;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.entity.UserLoginInfo;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationDeliveryTest extends AbstractTestContainers {

    @Test
    void taskToTaskNotificationDTOMapperTest(){
        Task dummyTask = new Task();
        User dummyAssigner = new User();
        UserLoginInfo dummyAssignerLoginInfo = new UserLoginInfo();
        User dummyAssignee = new User();
        UserLoginInfo dummyAssigneeLoginInfo = new UserLoginInfo();
        Event dummyEvent = new Event();

        dummyAssigner.setName("taskAssignerName");
        dummyAssignerLoginInfo.setEmail("taskAssignerEmail");
        dummyAssigner.setUserLoginInfo(dummyAssignerLoginInfo);
        dummyAssignee.setName("taskAssigneeName");
        dummyAssigneeLoginInfo.setEmail("taskAssigneeEmail");
        dummyAssignee.setUserLoginInfo(dummyAssigneeLoginInfo);
        dummyEvent.setTitle("taskEventName");

        dummyTask.setTitle("taskName");
        dummyTask.setAssigner(dummyAssigner);
        dummyTask.setAssignee(dummyAssignee);
        dummyTask.setEvent(dummyEvent);
        dummyTask.setStatus(TaskStatus.NEW);

        TaskNotificationDTO testMessage = TaskMapper.taskToTaskNotificationDTO(dummyTask);

        String expectedMessage = "Message content is: taskName, taskAssignerEmail, taskAssignerName, " +
                "taskAssigneeEmail, taskAssigneeName, taskEventName, NEW";

        String info = "Message content is: " + testMessage.taskName()
                + ", " + testMessage.taskAssignerEmail() + ", "
                + testMessage.taskAssignerName() + ", "
                + testMessage.taskAssigneeEmail() + ", "
                + testMessage.taskAssigneeName() + ", "
                + testMessage.taskEventName() + ", "
                + testMessage.taskStatus();

        assertEquals(expectedMessage, info);

    }

}
