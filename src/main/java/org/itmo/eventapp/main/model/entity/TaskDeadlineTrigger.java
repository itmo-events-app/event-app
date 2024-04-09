package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TaskDeadlineTrigger {
    @Id
    private Integer taskId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @PrimaryKeyJoinColumn(name = "task_id")
    private Task task;

    private LocalDateTime triggerTime;
}
