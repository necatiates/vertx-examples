package com.cas.spring.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by tolga on 13.03.2016.
 */
@Entity
public class GlobalSettings {

    @Id
    @Column
    private String name;

    @Column
    private String value;


    public String getName() {
        return name;
    }

    public void setName(String game) {
        this.name = game;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
