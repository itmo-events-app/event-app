package org.itmo.eventapp.main.model;

import jakarta.persistence.*;

@Converter(autoApply = true)
class PlaceFormatConverter implements AttributeConverter<PlaceFormat, String> {
    @Override
    public String convertToDatabaseColumn(PlaceFormat color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public PlaceFormat convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return PlaceFormat.valueOf(value.toUpperCase());
    }
}

@Entity
@Table(name = "place")
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "place_format", name = "format")
    @Convert(converter = PlaceFormatConverter.class)
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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PlaceFormat getFormat() {
        return format;
    }

    public void setFormat(PlaceFormat format) {
        this.format = format;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongtude() {
        return longtude;
    }

    public void setLongtude(float longtude) {
        this.longtude = longtude;
    }

    public String getRenderInfo() {
        return renderInfo;
    }

    public void setRenderInfo(String renderInfo) {
        this.renderInfo = renderInfo;
    }
}
