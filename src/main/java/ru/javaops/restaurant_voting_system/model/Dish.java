package ru.javaops.restaurant_voting_system.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "dish_unique_name")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    public Dish(Integer id, String name) {
        super(id, name);
    }

    public Dish(Dish d) {
        this(d.id, d.name);
    }
}
