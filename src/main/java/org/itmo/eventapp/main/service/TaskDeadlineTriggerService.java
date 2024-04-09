package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.TaskDeadlineTrigger;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.itmo.eventapp.main.repository.TaskDeadlineTriggerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskDeadlineTriggerService {
    private final TaskDeadlineTriggerRepository taskDeadlineTriggerRepository;
    private final TaskService taskService;

    @Transactional
    public List<Task> updateAndRetrieveTaskOnDeadline(LocalDateTime deadlineTime){
        List<Task> tasks = taskDeadlineTriggerRepository.findTasksByTriggerTimeBefore(deadlineTime);
        tasks.forEach(task -> taskService.setStatus(task.getId(), TaskStatus.EXPIRED));
        taskDeadlineTriggerRepository.deleteAllByTriggerTimeBefore(deadlineTime);
        return tasks;
    }

    public void createNewDeadlineTrigger(Task task){
        TaskDeadlineTrigger taskDeadlineTrigger = TaskDeadlineTrigger.builder()
                .task(task).taskId(task.getId()).triggerTime(task.getDeadline()).build();
        taskDeadlineTriggerRepository.save(taskDeadlineTrigger);
    }
}
