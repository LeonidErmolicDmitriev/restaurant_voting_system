package ru.javaops.restaurant_voting_system.web.menu;

import ru.javaops.restaurant_voting_system.model.*;
import ru.javaops.restaurant_voting_system.model.MenuItem;
import ru.javaops.restaurant_voting_system.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

public class MenuTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class);

    public static final int MENU_ITEM_ID_1 = 1;

    public static final int MENU_ITEM_ID_2 = 2;

    public static final int NOT_FOUND = 100;

    public static final Restaurant restaurant = new Restaurant(1, "Sushi com");

    public static final Restaurant restaurant2 = new Restaurant(2, "Pizza hut");

    public static final Restaurant restaurant3 = new Restaurant(3, "Как дома");

    public static final Restaurant restaurant4 = new Restaurant(4, "Есть все");

    public static final Dish dish1 = new Dish(1, "Philadelphia");

    public static final Dish dish3 = new Dish(3, "Green tea");

    public static final Dish dish7 = new Dish(7, "Coca cola");

    public static final Dish dish9 = new Dish(9, "Борщ");

    public static final Dish dish10 = new Dish(10, "Плов");

    public static final MenuItem menu_item_today1 = new MenuItem(MENU_ITEM_ID_1, restaurant, LocalDate.now(),
            dish1, 15000);

    public static final MenuItem menu_item_today2 = new MenuItem(MENU_ITEM_ID_2, restaurant, LocalDate.now(),
            dish3, 5000);

    public static final MenuItem menu_item_today3 = new MenuItem(3, restaurant2, LocalDate.now(),
            new Dish(5, "4 cheese"), 50000);

    public static final MenuItem menu_item_today4 = new MenuItem(4, restaurant2, LocalDate.now(), dish7, 5000);

    public static final MenuItem menu_item_today5 = new MenuItem(5, restaurant3, LocalDate.now(), dish9, 30000);

    public static final MenuItem menu_item_today6 = new MenuItem(6, restaurant3, LocalDate.now(), dish10, 40000);

    public static final MenuItem menu_item_today7 = new MenuItem(7, restaurant3, LocalDate.now(),
            new Dish(13, "Компот"), 10000);

    public static final MenuItem menu_item_today8 = new MenuItem(8, restaurant4, LocalDate.now(), dish9, 35000);

    public static final MenuItem menu_item_today9 = new MenuItem(9, restaurant4, LocalDate.now(), dish10, 35000);

    public static final MenuItem menu_item_today10 = new MenuItem(10, restaurant4, LocalDate.now(), dish7, 5500);

    public static final List<MenuItem> restaurant_today_menu = List.of(menu_item_today1, menu_item_today2);

    public static final List<MenuItem> today_menu = List.of(menu_item_today3, menu_item_today4, menu_item_today1,
            menu_item_today2, menu_item_today10, menu_item_today8, menu_item_today9, menu_item_today5, menu_item_today6,
            menu_item_today7);

    public static MenuItem getNew() {
        return new MenuItem(null, restaurant2, LocalDate.now(), dish10, 100);
    }

    public static MenuItem getUpdated() {
        return new MenuItem(MENU_ITEM_ID_2, restaurant2, LocalDate.now(), dish10, 200);
    }
}
