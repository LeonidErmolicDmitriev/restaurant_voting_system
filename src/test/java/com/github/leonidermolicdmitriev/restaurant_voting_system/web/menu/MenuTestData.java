package com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.MenuItem;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.MenuItemTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuTestData {
    public static final MatcherFactory.Matcher<MenuItemTo> MENU_ITEM_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(MenuItemTo.class);

    public static final int MENU_ITEM_ID_1 = 1;

    public static final int MENU_ITEM_ID_2 = 2;

    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant = new Restaurant(1, "Sushi com");

    public static final Dish dish1 = new Dish(1, "Philadelphia", restaurant);

    public static final Dish dish3 = new Dish(3, "Green tea", restaurant);

    public static final Dish test_dish = new Dish(14, "Тесто", restaurant);

    public static final MenuItem menu_item_today1 = new MenuItem(MENU_ITEM_ID_1, restaurant, LocalDate.now(),
            dish1, 15000);

    public static final MenuItem menu_item_today2 = new MenuItem(MENU_ITEM_ID_2, restaurant, LocalDate.now(),
            dish3, 5000);

    public static final List<MenuItem> today_menu = List.of(menu_item_today1, menu_item_today2);

    public static MenuItem getNew() {
        return new MenuItem(null, restaurant, LocalDate.now(), test_dish, 100);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID_2, restaurant, LocalDate.now(), test_dish, 200);
    }
}
