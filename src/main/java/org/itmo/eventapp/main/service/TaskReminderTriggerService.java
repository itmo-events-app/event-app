package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.TaskReminderTrigger;
import org.itmo.eventapp.main.repository.TaskReminderTriggerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskReminderTriggerService {
    private final TaskReminderTriggerRepository taskReminderTriggerRepository;

    @Transactional
    public List<Task> retrieveTasksOnReminder(LocalDateTime reminderTime){
        List<Task> tasks = taskReminderTriggerRepository.findTasksByTriggerTimeBefore(reminderTime);
        taskReminderTriggerRepository.deleteAllByTriggerTimeBefore(reminderTime);
        return tasks;
    }

    public void createNewReminderTrigger(Task task){
        TaskReminderTrigger taskReminderTrigger = TaskReminderTrigger.builder()
                .task(task).triggerTime(task.getReminder()).build();
        taskReminderTriggerRepository.save(taskReminderTrigger);
    }
}
