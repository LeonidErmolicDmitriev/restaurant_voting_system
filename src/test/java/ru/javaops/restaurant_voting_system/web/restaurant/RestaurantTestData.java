package ru.javaops.restaurant_voting_system.web.restaurant;

import ru.javaops.restaurant_voting_system.model.Restaurant;
import ru.javaops.restaurant_voting_system.web.MatcherFactory;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class);

    public static final int RESTAURANT_1_ID = 1;

    public static final int RESTAURANT_2_ID = 2;

    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT_1_ID, "Sushi com");

    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT_2_ID, "Pizza hut");

    public static final Restaurant restaurant3 = new Restaurant(3, "Как дома");

    public static final Restaurant restaurant4 = new Restaurant(4, "Есть все");

    public static final List<Restaurant> restaurants = List.of(restaurant2, restaurant1, restaurant4, restaurant3);

    public static Restaurant getNew() {
        return new Restaurant(null, "New");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT_2_ID, "UpdatedName");
    }
}
