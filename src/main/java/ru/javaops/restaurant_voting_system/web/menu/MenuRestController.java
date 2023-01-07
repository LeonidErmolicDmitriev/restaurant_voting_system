package ru.javaops.restaurant_voting_system.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javaops.restaurant_voting_system.model.MenuItem;
import ru.javaops.restaurant_voting_system.repository.MenuItemRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "menu_items")
public class MenuRestController extends AbstractMenuController {

    static final String REST_URL = "/api/menu_items";

    public MenuRestController(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    @GetMapping("/by_date")
    @Cacheable
    public List<MenuItem> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getByDate(date);
    }

    @GetMapping("/today")
    @Cacheable
    public List<MenuItem> getByToday() {
        return super.getByToday();
    }

    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<MenuItem> get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping("/today/restaurant/{id}")
    @Cacheable
    public List<MenuItem> getByRestaurantId(@PathVariable(name = "id") int restaurantId) {
        return super.getByTodayByRestaurant(restaurantId);
    }
}