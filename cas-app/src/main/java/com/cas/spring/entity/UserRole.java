package com.cas.spring.entity;

import javax.persistence.*;

/**
 * Created by tolga on 12.03.2016.
 */

@Entity
@Table(name="user_roles")
public class UserRole {

    @Id
    @Column(name="userrole_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "username")

    private User username;

    @Column
    private String role;

    public User getUser() {
        return username;
    }

    public void setUser(User username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
