package ru.javaops.restaurant_voting_system.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Entity
@Table(name = "menu_items", uniqueConstraints = {@UniqueConstraint(columnNames = {"restaurant_id", "dish_id", "date"}, name = "menu_unique_restaurant_dish_date")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
public class MenuItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @NotNull
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @Column(name = "date", nullable = false)
    @NotNull
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    @NotNull
    @ToString.Exclude
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Dish dish;

    @Column(name = "price", nullable = false)
    @NotNull
    @Positive
    private Integer price; //cents

    public MenuItem(Integer id, Restaurant restaurant, LocalDate date, Dish dish, Integer price) {
        super(id);
        this.restaurant = restaurant;
        this.date = date;
        this.dish = dish;
        this.price = price;
    }

    public int getRestaurantId() {
        if (restaurant == null) {
            return 0;
        }
        Integer restaurantId = restaurant.getId();
        return (restaurantId == null) ? 0 : restaurantId;
    }

    public int getDishId() {
        if (dish == null) {
            return 0;
        }
        Integer dishId = dish.getId();
        return (dishId == null) ? 0 : dishId;
    }
}
