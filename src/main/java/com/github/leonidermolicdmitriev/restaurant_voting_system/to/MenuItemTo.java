package com.github.leonidermolicdmitriev.restaurant_voting_system.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MenuItemTo extends BaseTo {

    @PositiveOrZero
    int restaurantId;

    @NotNull
    LocalDate registeredDate;

    @PositiveOrZero
    int dishId;

    @PositiveOrZero
    int price; //cents


    public MenuItemTo(Integer id, int restaurantId, LocalDate registeredDate, int dishId, int price) {
        super(id);
        this.restaurantId = restaurantId;
        this.registeredDate = registeredDate;
        this.dishId = dishId;
        this.price = price;
    }
}
