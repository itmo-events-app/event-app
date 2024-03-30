package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }
}
