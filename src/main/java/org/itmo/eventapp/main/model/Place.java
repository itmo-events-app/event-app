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

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private PlaceFormat format;

    @Column(name = "room")
    private String room;

    @Column(name = "description")
    private String description;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longtude")
    private float longtude;

    @Column(name = "render_info")
    private String renderInfo;

    public Place() {
    }

    public Place(String name, String address, PlaceFormat format, String room, String description, float latitude, float longtude, String renderInfo) {
        this.name = name;
        this.address = address;
        this.format = format;
        this.room = room;
        this.description = description;
        this.latitude = latitude;
        this.longtude = longtude;
        this.renderInfo = renderInfo;
    }
}
