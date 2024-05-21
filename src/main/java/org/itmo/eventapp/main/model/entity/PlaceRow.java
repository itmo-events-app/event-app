package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlaceRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    private Place place;
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;


}
