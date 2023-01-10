package com.github.leonidermolicdmitriev.restaurant_voting_system.web.dish;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.DishTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<DishTo> DISH_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(DishTo.class);

    public static final int DISH_1_ID = 1;

    public static final int DISH_2_ID = 2;

    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant = new Restaurant(1, "Sushi com");

    public static final Restaurant restaurant3 = new Restaurant(3, "Как дома");

    public static final Dish dish1 = new Dish(DISH_1_ID, "Philadelphia", restaurant);

    public static final Dish dish2 = new Dish(DISH_2_ID, "Spice herring", restaurant);

    public static final Dish dish3 = new Dish(3, "Green tea", restaurant);

    public static final Dish dish4 = new Dish(4, "Latte", restaurant);

    public static final Dish dish14 = new Dish(14, "Тесто", restaurant);

    public static final List<Dish> dishes = List.of(dish3, dish4, dish1, dish2, dish14);

    public static Dish getNew() {
        return new Dish(null, "New", restaurant3);
    }

    public static Dish getUpdated() {
        return new Dish(DISH_2_ID, "UpdatedName", restaurant3);
    }
}
