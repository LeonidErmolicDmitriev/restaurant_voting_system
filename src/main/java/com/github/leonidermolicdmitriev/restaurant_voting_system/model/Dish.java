package com.github.leonidermolicdmitriev.restaurant_voting_system.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dishes", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "restaurant_id"},
        name = "dish_unique_name_restaurant")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Dish(Integer id, String name, Restaurant restaurant) {
        super(id, name);
        this.restaurant = restaurant;
    }

    public Dish(Dish d) {
        this(d.id, d.name, d.restaurant);
    }
}
