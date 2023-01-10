package com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu;

import com.github.leonidermolicdmitriev.restaurant_voting_system.to.MenuItemTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.MenuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.MenuItemRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public abstract class AbstractMenuController {

    protected final MenuItemRepository menuItemRepository;

    protected AbstractMenuController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItemTo> getByDate(LocalDate date, int restaurantId) {
        log.info("get by date {} for restaurantId={}", date, restaurantId);
        return MenuUtil.menuItemToTos(menuItemRepository.getByDateAndRestaurantId(date, restaurantId));
    }

    public ResponseEntity<MenuItemTo> get(int id, int restaurantId) {
        log.info("get menu item by id {} with restaurantId={}", id, restaurantId);
        return ResponseEntity.of(MenuUtil.getTo(menuItemRepository.getByIdAndRestaurantId(id, restaurantId)));
    }

    public List<MenuItemTo> getByTodayByRestaurant(int restaurantId) {
        LocalDate date = LocalDate.now();
        log.info("get by today for restaurantId {}", restaurantId);
        return MenuUtil.menuItemToTos(menuItemRepository.getByDateAndRestaurantId(date, restaurantId));
    }
}