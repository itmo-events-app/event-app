package org.itmo.eventapp.main.service.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.itmo.eventapp.main.model.entity.Task;
import org.itmo.eventapp.main.model.entity.enums.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class TaskSpecification {

    private TaskSpecification() {

    }

    public static Specification<Task> filterByEventIdAndExtraParams(Integer eventId,
                                                                    Integer assigneeId,
                                                                    Integer assignerId,
                                                                    TaskStatus taskStatus,
                                                                    LocalDateTime deadlineLowerLimit,
                                                                    LocalDateTime deadlineUpperLimit) {
        return Specification
                .where(hasEventId(eventId))
                .and(matchesFilter(assigneeId,
                        assignerId,
                        taskStatus,
                        deadlineLowerLimit,
                        deadlineUpperLimit));
    }

    public static Specification<Task> filterByEventIdsListAndExtraParams(List<Integer> eventIds,
                                                                         Integer assigneeId,
                                                                         Integer assignerId,
                                                                         TaskStatus taskStatus,
                                                                         LocalDateTime deadlineLowerLimit,
                                                                         LocalDateTime deadlineUpperLimit) {
        return Specification
                .where(hasEventIdInList(eventIds))
                .and(matchesFilter(assigneeId,
                        assignerId,
                        taskStatus,
                        deadlineLowerLimit,
                        deadlineUpperLimit));
    }

    static Specification<Task> matchesFilter(Integer assigneeId,
                                             Integer assignerId,
                                             TaskStatus taskStatus,
                                             LocalDateTime deadlineLowerLimit,
                                             LocalDateTime deadlineUpperLimit) {
        return Specification
                .where(hasAssigneeId(assigneeId))
                .and(hasAssignerId(assignerId))
                .and(hasTaskStatus(taskStatus))
                .and(hasDeadlineUpperLimit(deadlineUpperLimit))
                .and(hasDeadlineLowerLimit(deadlineLowerLimit));
    }

    static Specification<Task> hasAssigneeId(Integer assigneeId) {
        return (root, cq, cb) -> assigneeId == null ? cb.conjunction() : cb.equal(root.get("assignee").get("id"), assigneeId);
    }

    static Specification<Task> hasAssignerId(Integer assignerId) {
        return (root, cq, cb) -> assignerId == null ? cb.conjunction() : cb.equal(root.get("assigner").get("id"), assignerId);
    }

    static Specification<Task> hasTaskStatus(TaskStatus taskStatus) {
        return (root, cq, cb) -> taskStatus == null ? cb.conjunction() : cb.equal(root.get("status"), taskStatus);
    }

    static Specification<Task> hasDeadlineUpperLimit(LocalDateTime deadlineUpperLimit) {
        return (root, cq, cb) -> deadlineUpperLimit == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("deadline"), deadlineUpperLimit);
    }

    static Specification<Task> hasDeadlineLowerLimit(LocalDateTime deadlineLowerLimit) {
        return (root, cq, cb) -> deadlineLowerLimit == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("deadline"), deadlineLowerLimit);
    }

    static Specification<Task> hasEventId(Integer eventId) {
        return (root, cq, cb) -> eventId == null ? cb.conjunction() : cb.equal(root.get("event").get("id"), eventId);
    }

    static Specification<Task> hasEventIdInList(List<Integer> eventIds) {
        return (root, cq, cb) -> {
            if (eventIds == null) {
                return cb.conjunction();
            }
            CriteriaBuilder.In<Integer> inClause = cb.in(root.get("event").get("id"));
            for (Integer eventId : eventIds) {
                inClause.value(eventId);
            }
            return inClause;
        };

    }


}
