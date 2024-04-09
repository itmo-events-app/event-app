package org.itmo.eventapp.main.security.securityexpression;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.itmo.eventapp.main.model.dto.request.UserShortDataRequest;
import org.itmo.eventapp.main.model.entity.*;
import org.itmo.eventapp.main.model.entity.enums.PrivilegeName;
import org.itmo.eventapp.main.service.EventRoleService;
import org.itmo.eventapp.main.service.EventService;
import org.itmo.eventapp.main.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Validated
@Service
public class TaskSecurityExpression {
    private final TaskService taskService;

    private final MiscSecurityExpression miscSecurityExpression;

    private int getTaskParentEventId(int taskId) {

        Event event = taskService.findById(taskId).getEvent();
        return (event.getParent() == null) ? event.getId() : event.getParent().getId();
    }

    public boolean canCreateTask(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {

        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.CREATE_TASK);
    }

    public boolean canEditTask(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {

        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.EDIT_TASK);

    }

    public boolean canDeleteTask(@Min(value = 1, message = "Параметр taskId не может быть меньше 1!") int taskId) {

        int eventId = getTaskParentEventId(taskId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.DELETE_TASK);

    }

    public boolean canEditTaskStatus(@Min(value = 1, message = "Параметр taskId не может быть меньше 1!") int taskId) {

        Task task = taskService.findById(taskId);
        Event event = (task.getEvent().getParent() == null) ? task.getEvent() : task.getEvent().getParent();

        int userId = miscSecurityExpression.getCurrentUserId();

        Stream<Privilege> eventPrivileges = miscSecurityExpression.getUserEventPrivileges(event.getId(), userId);

        return eventPrivileges.map(Privilege::getName).anyMatch(it -> {
                    boolean canEdit = it.equals(PrivilegeName.CHANGE_TASK_STATUS);
                    boolean canEditAsAssignee = it.equals(PrivilegeName.CHANGE_ASSIGNED_TASK_STATUS)
                            && task.getAssignee() != null
                            && userId == task.getAssignee().getId();
                    return canEdit || canEditAsAssignee;
                }
        );
    }

    public boolean canEditTaskAssignee(@Min(value = 1, message = "Параметр taskId не может быть меньше 1!") int taskId) {

        int eventId = getTaskParentEventId(taskId);
        Stream<Privilege> eventPrivileges = miscSecurityExpression.getUserEventPrivileges(eventId, miscSecurityExpression.getCurrentUserId());
        return eventPrivileges.map(Privilege::getName).anyMatch(it -> it.equals(PrivilegeName.ASSIGN_TASK_EXECUTOR)
                || it.equals(PrivilegeName.REPLACE_TASK_EXECUTOR));
    }

    public boolean canDeleteTaskAssignee(@Min(value = 1, message = "Параметр taskId не может быть меньше 1!") int taskId) {

        Task task = taskService.findById(taskId);
        Event event = (task.getEvent().getParent() == null) ? task.getEvent() : task.getEvent().getParent();

        int userId = miscSecurityExpression.getCurrentUserId();

        Stream<Privilege> eventPrivileges = miscSecurityExpression.getUserEventPrivileges(event.getId(), userId);
        return eventPrivileges.map(Privilege::getName).anyMatch(it -> {
            boolean canDelete = it.equals(PrivilegeName.DELETE_TASK_EXECUTOR);
            boolean canDeleteSelfFromAssignee = it.equals(PrivilegeName.DECLINE_TASK_EXECUTION)
                    && task.getAssignee() != null
                    && userId == task.getAssignee().getId();
            return canDelete || canDeleteSelfFromAssignee;
        });
    }

    public boolean canTakeOnTask(@Min(value = 1, message = "Параметр taskId не может быть меньше 1!") int taskId) {

        int eventId = getTaskParentEventId(taskId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.ASSIGN_SELF_AS_TASK_EXECUTOR);
    }

    public boolean canGetEventTasks(@Min(value = 1, message = "Параметр eventId не может быть меньше 1!") int eventId) {

        eventId = miscSecurityExpression.getParentEventOrSelfId(eventId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.VIEW_ALL_EVENT_TASKS);
    }

    public boolean canGetTask(@Min(value = 1, message = "Параметр taskId не может быть меньше 1!") int taskId) {
        int eventId = getTaskParentEventId(taskId);
        return miscSecurityExpression.checkEventPrivilege(eventId, PrivilegeName.VIEW_ALL_EVENT_TASKS);
    }
}