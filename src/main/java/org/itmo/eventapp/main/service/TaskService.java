package org.itmo.eventapp.main.service;

import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.TaskRequest;
import org.itmo.eventapp.main.model.entity.Event;
import org.itmo.eventapp.main.model.entity.Place;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.User;
import org.itmo.eventapp.main.model.mapper.TaskMapper;
import org.itmo.eventapp.main.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final EventService eventService;
    private final TaskRepository taskRepository;

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    public Task save(TaskRequest taskRequest) {
        // Event event = eventService.findById(taskRequest.eventId());
        // TODO, example above
        // task assigner from principal.name -> findByUsername
        Event event = new Event();
        User assigner = new User();
        User assignee = new User();
        Place place = new Place();

        return taskRepository.save(TaskMapper.taskRequestToTask(taskRequest, event, assignee, assigner, place));
    }
}
