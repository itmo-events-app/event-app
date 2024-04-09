package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.repository.TaskDeadlineTriggerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskReminderTriggerService {
    private final TaskDeadlineTriggerRepository taskDeadlineTriggerRepository;

    @Transactional
    public List<Task> retrieveTasksOnReminder(LocalDateTime reminderTime){
        List<Task> tasks = taskDeadlineTriggerRepository.findTasksByTriggerTimeBefore(reminderTime);
        taskDeadlineTriggerRepository.deleteAllByTriggerTimeBefore(reminderTime);
        return tasks;
    }
}