package ru.javaops.restaurant_voting_system.web.dish;

import ru.javaops.restaurant_voting_system.model.Dish;
import ru.javaops.restaurant_voting_system.web.MatcherFactory;

import java.util.List;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class);

    public static final int DISH_1_ID = 1;

    public static final int DISH_2_ID = 2;

    public static final int NOT_FOUND = 100;

    public static final Dish dish1 = new Dish(DISH_1_ID, "Philadelphia");

    public static final Dish dish2 = new Dish(DISH_2_ID, "Spice herring");

    public static final Dish dish3 = new Dish(3, "Green tea");

    public static final Dish dish4 = new Dish(4, "Latte");

    public static final Dish dish5 = new Dish(5, "4 cheese");

    public static final Dish dish6 = new Dish(6, "Rancho");

    public static final Dish dish7 = new Dish(7, "Coca cola");

    public static final Dish dish8 = new Dish(8, "Sprite");

    public static final Dish dish9 = new Dish(9, "Борщ");

    public static final Dish dish10 = new Dish(10, "Плов");

    public static final Dish dish11 = new Dish(11, "Зама");

    public static final Dish dish12 = new Dish(12, "Картошка по-домашнему");

    public static final Dish dish13 = new Dish(13, "Компот");

    public static final List<Dish> dishes = List.of(dish5, dish7, dish3, dish4, dish1, dish6, dish2, dish8, dish9,
            dish11, dish12, dish13, dish10);

    public static Dish getNew() {
        return new Dish(null, "New");
    }

    public static Dish getUpdated() {
        return new Dish(DISH_2_ID, "UpdatedName");
    }
}
