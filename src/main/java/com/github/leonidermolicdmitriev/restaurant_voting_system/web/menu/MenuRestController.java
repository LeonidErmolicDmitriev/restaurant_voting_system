package com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu;

import com.github.leonidermolicdmitriev.restaurant_voting_system.to.MenuItemTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.MenuItemRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = MenuRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "menu_items")
public class MenuRestController extends AbstractMenuController {

    static final String REST_URL = "/api/restaurants/{restaurantId}/menu";

    public MenuRestController(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    @GetMapping("/by-date")
    @Cacheable(key = "{#date, #restaurantId}")
    @Override
    public List<MenuItemTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      @PathVariable int restaurantId) {
        return super.getByDate(date, restaurantId);
    }

    @GetMapping
    @Cacheable(key = "{'Today', #restaurantId}")
    @Override
    public List<MenuItemTo> getByTodayByRestaurant(@PathVariable int restaurantId) {
        return super.getByTodayByRestaurant(restaurantId);
    }
}