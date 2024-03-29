package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "participant")
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    @Column(name = "additional_info")
    private String additionalInfo;

    private boolean visited;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;

    public Participant() {
    }

    public Participant(String name, String email, String additionalInfo, boolean visited, Event event) {
        this.name = name;
        this.email = email;
        this.additionalInfo = additionalInfo;
        this.visited = visited;
        this.event = event;
    }
}
