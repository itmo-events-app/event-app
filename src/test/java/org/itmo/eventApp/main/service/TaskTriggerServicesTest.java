package org.itmo.eventApp.main.service;

import org.itmo.eventApp.main.controller.AbstractTestContainers;
import org.itmo.eventapp.main.model.entity.TaskDeadlineTrigger;
import org.itmo.eventapp.main.repository.TaskDeadlineTriggerRepository;
import org.itmo.eventapp.main.repository.TaskReminderTriggerRepository;
import org.itmo.eventapp.main.service.TaskDeadlineTriggerService;
import org.itmo.eventapp.main.service.TaskReminderTriggerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTriggerServicesTest extends AbstractTestContainers {
    @Autowired
    private TaskReminderTriggerService taskReminderTriggerService;

    @Autowired
    private TaskDeadlineTriggerService taskDeadlineTriggerService;

    @Autowired
    private TaskReminderTriggerRepository taskReminderTriggerRepository;

    @Autowired
    private TaskDeadlineTriggerRepository taskDeadlineTriggerRepository;


    private void databaseFilling(){
        executeSqlScript("/sql/insert_user.sql");
        executeSqlScript("/sql/insert_user_2.sql");
        executeSqlScript("/sql/insert_place.sql");
        executeSqlScript("/sql/insert_event.sql");
        executeSqlScript("/sql/insert_event_2.sql");
        executeSqlScript("/sql/insert_event_role.sql");
        executeSqlScript("/sql/insert_task.sql");
        executeSqlScript("/sql/insert_task_2.sql");
        executeSqlScript("/sql/insert_task_triggers.sql");
        executeSqlScript("/sql/insert_task_triggers_2.sql");
    }

    @Test
    void updateAndRetrieveTaskOnDeadlineTest(){
        databaseFilling();
        LocalDateTime triggerTime = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        taskDeadlineTriggerService.updateAndRetrieveTaskOnDeadline(triggerTime);
        List<TaskDeadlineTrigger> triggers = taskDeadlineTriggerRepository.findAll();
        assertTrue(triggers.isEmpty());
    }

    @Test
    void retrieveTasksOnReminderTest(){
        databaseFilling();
        LocalDateTime triggerTime = LocalDateTime.of(2025, 4, 20, 21, 0, 0);
        taskReminderTriggerService.retrieveTasksOnReminder(triggerTime);
        List<TaskDeadlineTrigger> triggers = taskReminderTriggerRepository.findAll();
        assertTrue(triggers.isEmpty());
    }
}
