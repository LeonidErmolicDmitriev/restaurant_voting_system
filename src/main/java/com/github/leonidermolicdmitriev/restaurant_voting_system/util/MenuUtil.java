package com.github.leonidermolicdmitriev.restaurant_voting_system.util;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.MenuItem;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.MenuItemTo;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class MenuUtil {
    public static MenuItem createNewFromTo(MenuItemTo menuItemTo, Restaurant restaurant, Dish dish) {
        return new MenuItem(null, restaurant, menuItemTo.getRegisteredDate(), dish, menuItemTo.getPrice());
    }

    public MenuItemTo createTo(MenuItem menuItem, Restaurant restaurant, Dish dish) {
        return new MenuItemTo(menuItem.getId(), restaurant.getId(), menuItem.getRegisteredDate(),
                dish.getId(), menuItem.getPrice());
    }

    public static List<MenuItemTo> menuItemToTos(List<MenuItem> menuItems) {
        return menuItems.stream()
                .map(menuItem -> MenuUtil.createTo(menuItem, menuItem.getRestaurant(), menuItem.getDish()))
                .collect(Collectors.toList());
    }

    public static Optional<MenuItemTo> getTo(Optional<MenuItem> optionalMenuItem) {
        return optionalMenuItem.map(menuItem -> MenuUtil.createTo(menuItem, menuItem.getRestaurant(), menuItem.getDish()));
    }
}