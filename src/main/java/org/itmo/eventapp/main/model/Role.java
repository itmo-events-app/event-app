package org.itmo.eventapp.main.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private RoleType type;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name="privileges",
            joinColumns=  @JoinColumn(name="role_id", referencedColumnName="id"),
            inverseJoinColumns= @JoinColumn(name="privileges_id", referencedColumnName="id") )
    private Set<Privilege> privileges = new HashSet<Privilege>();

    public Role() {
    }

    public Role(String name, String description, RoleType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }
}
