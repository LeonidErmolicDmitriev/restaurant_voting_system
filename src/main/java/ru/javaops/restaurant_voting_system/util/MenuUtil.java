package ru.javaops.restaurant_voting_system.util;

import lombok.experimental.UtilityClass;
import ru.javaops.restaurant_voting_system.model.*;
import ru.javaops.restaurant_voting_system.to.MenuItemTo;

import javax.persistence.EntityManager;

@UtilityClass
public class MenuUtil {
    public static MenuItem createNewFromTo(MenuItemTo menuItemTo, EntityManager entityManager) {
        Restaurant restaurant = entityManager.find(Restaurant.class, menuItemTo.getRestaurantId());
        Dish dish = entityManager.find(Dish.class, menuItemTo.getDishId());
        return new MenuItem(null, restaurant, menuItemTo.getDate(), dish, menuItemTo.getPrice());
    }

    public static MenuItem updateFromTo(MenuItem menuItem, MenuItemTo menuItemTo, EntityManager entityManager) {
        Restaurant restaurant = entityManager.find(Restaurant.class, menuItemTo.getRestaurantId());
        Dish dish = entityManager.find(Dish.class, menuItemTo.getDishId());
        menuItem.setRestaurant(restaurant);
        menuItem.setDate(menuItemTo.getDate());
        menuItem.setDish(dish);
        menuItem.setPrice(menuItemTo.getPrice());
        return menuItem;
    }
}