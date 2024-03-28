package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "place")
@Getter
@Setter
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String address;

    @Enumerated(EnumType.STRING)
    private PlaceFormat format;

    private String room;

    private String description;

    private float latitude;

    private float longitude;

    private String renderInfo;

    public Place() {
    }

    public Place(String name, String address, PlaceFormat format, String room, String description, float latitude, float longitude, String renderInfo) {
        this.name = name;
        this.address = address;
        this.format = format;
        this.room = room;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.renderInfo = renderInfo;
    }
}
