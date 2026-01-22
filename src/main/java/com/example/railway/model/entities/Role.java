package com.example.railway.model.entities;

import jakarta.persistence.*;
import com.example.railway.model.enums.UserRoles;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private UserRoles name;

    public Role() {
    }

    public Role(UserRoles name) {
        this.name = name;
    }

    public UserRoles getName() {
        return name;
    }

    public void setName(UserRoles name) {
        this.name = name;
    }
}
