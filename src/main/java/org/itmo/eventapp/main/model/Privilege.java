package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

@Converter(autoApply = true)
class PrivilegeTypeConverter implements AttributeConverter<PrivilegeType, String> {
    @Override
    public String convertToDatabaseColumn(PrivilegeType color) {
        if (color == null) {
            return null;
        }
        return color.name().toLowerCase();
    }

    @Override
    public PrivilegeType convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return PrivilegeType.valueOf(value.toUpperCase());
    }
}

@Entity
@Table(name = "privilege")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "privilege_type", name = "type")
    @Convert(converter = PrivilegeTypeConverter.class)
    private PrivilegeType type;

    public Privilege() {
    }

    public Privilege(String name, String description, PrivilegeType type) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PrivilegeType getType() {
        return type;
    }

    public void setType(PrivilegeType type) {
        this.type = type;
    }
}
