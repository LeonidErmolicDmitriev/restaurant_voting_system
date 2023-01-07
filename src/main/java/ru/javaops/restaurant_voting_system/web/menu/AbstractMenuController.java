package ru.javaops.restaurant_voting_system.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.javaops.restaurant_voting_system.model.MenuItem;
import ru.javaops.restaurant_voting_system.repository.MenuItemRepository;

import java.time.LocalDate;
import java.util.List;

@Slf4j
public abstract class AbstractMenuController {

    protected final MenuItemRepository menuItemRepository;

    public AbstractMenuController(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getByDate(LocalDate date) {
        log.info("getByDate");
        return getAllByDate(date);
    }

    public List<MenuItem> getByToday() {
        LocalDate date = LocalDate.now();
        log.info("getByToday");
        return getAllByDate(date);
    }

    private List<MenuItem> getAllByDate(LocalDate date) {
        log.info("get by date {}", date);
        return menuItemRepository.findAllByDate(date);
    }

    public ResponseEntity<MenuItem> get(int id) {
        log.info("get menu item by id {}", id);
        return ResponseEntity.of(menuItemRepository.getByIdWithRestaurantAndDish(id));
    }

    public List<MenuItem> getByTodayByRestaurant(int resraurantId) {
        LocalDate date = LocalDate.now();
        log.info("getByTodayByRestaurant");
        return menuItemRepository.getByDateAndRestaurantId(date, resraurantId);
    }
}