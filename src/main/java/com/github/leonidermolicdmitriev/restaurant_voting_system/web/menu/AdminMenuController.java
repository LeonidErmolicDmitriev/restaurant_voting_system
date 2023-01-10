package com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.DishRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.RestaurantRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.MenuItemTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.MenuUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.MenuItem;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.MenuItemRepository;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.leonidermolicdmitriev.restaurant_voting_system.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "menu_items")
public class AdminMenuController extends AbstractMenuController {

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/menu";

    private final RestaurantRepository restaurantRepository;

    private final DishRepository dishRepository;

    public AdminMenuController(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository,
                               DishRepository dishRepository) {
        super(menuItemRepository);
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping
    @Cacheable
    @Override
    public List<MenuItemTo> getByTodayByRestaurant(@PathVariable int restaurantId) {
        return super.getByTodayByRestaurant(restaurantId);
    }

    @GetMapping("/by-date")
    @Override
    public List<MenuItemTo> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      @PathVariable int restaurantId) {
        return super.getByDate(date, restaurantId);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<MenuItemTo> get(@PathVariable int id, @PathVariable int restaurantId) {
        return super.get(id, restaurantId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    @Transactional
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete menu item by id {}", id);
        checkIfMenuItemExist(menuItemRepository, id, restaurantId);
        menuItemRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItemTo> createWithLocation(@Valid @RequestBody MenuItemTo menuItemTo,
                                                         @PathVariable int restaurantId) {
        log.info("create menu item {}", menuItemTo);
        checkNew(menuItemTo);
        Restaurant restaurant = restaurantRepository.getById(restaurantId);
        Dish dish = dishRepository.getBodyIfExist(menuItemTo.getDishId(), restaurantId);
        MenuItem menuItem = MenuUtil.createNewFromTo(menuItemTo, restaurant, dish);
        MenuItem created = menuItemRepository.save(menuItem);
        Map<String, Integer> urlParams = new HashMap<>();
        urlParams.put("restaurantId", restaurantId);
        urlParams.put("id", created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(urlParams).toUri();
        return ResponseEntity.created(uriOfNewResource).body(MenuUtil.createTo(created, restaurant, dish));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody MenuItemTo menuItemTo, @PathVariable int id,
                       @PathVariable int restaurantId) {
        log.info("update menu item {} with id={}", menuItemTo, id);
        assureIdConsistent(menuItemTo, id);
        Restaurant restaurant = restaurantRepository.getById(restaurantId);
        Dish dish = dishRepository.getBodyIfExist(menuItemTo.getDishId(), restaurantId);
        MenuItem menuItem = MenuUtil.createNewFromTo(menuItemTo, restaurant, dish);
        menuItem.setId(menuItemTo.getId());
        menuItemRepository.save(menuItem);
    }
}