package org.itmo.eventapp.main.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "privilege")
@Getter
@Setter
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private PrivilegeName name;

    private String description;

    @Enumerated(EnumType.STRING)
    private PrivilegeType type;

    public Privilege() {
    }

    public Privilege(String name, String description, PrivilegeType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
