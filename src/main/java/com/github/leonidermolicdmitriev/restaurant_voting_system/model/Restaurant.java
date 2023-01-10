package com.github.leonidermolicdmitriev.restaurant_voting_system.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "restaurants", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "restaurant_unique_name")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {
    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public Restaurant(Restaurant r) {
        this(r.id, r.name);
    }
}
