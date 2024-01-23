package com.lab.model.model;

import com.lab.model.config.util.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter @Setter
@Entity
@Table(name = "role")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* See Role enum class from config.util for the default roles */
    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<UserEntity> users = new ArrayList<>();

    public void addUser(UserEntity user) {
        users.add(user);
    }

    public void setRole(Role role) {
        this.name= role.name();
    }
}
