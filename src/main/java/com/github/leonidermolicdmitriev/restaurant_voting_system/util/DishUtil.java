package com.github.leonidermolicdmitriev.restaurant_voting_system.util;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.DishTo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DishUtil {
    public static List<DishTo> dishesToTos(List<Dish> dishes) {
        return dishes.stream().map(DishUtil::createTo).collect(Collectors.toList());
    }

    public static Optional<DishTo> getTo(Optional<Dish> optionalDish) {
        return optionalDish.map(DishUtil::createTo);
    }

    public static Dish createNewFromTo(DishTo dishTo, Restaurant restaurant) {
        return new Dish(dishTo.getId(), dishTo.getName(), restaurant);
    }

    public static DishTo createTo(Dish dish) {
        return new DishTo(dish.getId(), dish.getName());
    }

    public static void updateFromTo(Dish dish, DishTo dishTo) {
        dish.setName(dishTo.getName());
    }
}
