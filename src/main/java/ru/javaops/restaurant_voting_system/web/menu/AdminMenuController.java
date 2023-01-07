package ru.javaops.restaurant_voting_system.web.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting_system.model.MenuItem;
import ru.javaops.restaurant_voting_system.repository.MenuItemRepository;
import ru.javaops.restaurant_voting_system.to.MenuItemTo;
import ru.javaops.restaurant_voting_system.util.MenuUtil;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javaops.restaurant_voting_system.util.validation.ValidationUtil.assureIdConsistent;
import static ru.javaops.restaurant_voting_system.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = AdminMenuController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@CacheConfig(cacheNames = "menu_items")
public class AdminMenuController extends AbstractMenuController {

    static final String REST_URL = "/api/admin/menu_items";

    private final EntityManager entityManager;

    public AdminMenuController(MenuItemRepository menuItemRepository, EntityManager entityManager) {
        super(menuItemRepository);
        this.entityManager = entityManager;
    }

    @GetMapping("/by_date")
    @Cacheable
    public List<MenuItem> getByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return super.getByDate(date);
    }

    @GetMapping("/{id}")
    @Cacheable
    public ResponseEntity<MenuItem> get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete menu item by id {}", id);
        menuItemRepository.deleteExisted(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(allEntries = true)
    public ResponseEntity<MenuItem> createWithLocation(@Valid @RequestBody MenuItemTo menuItemTo) {
        log.info("create menu item {}", menuItemTo);
        checkNew(menuItemTo);
        MenuItem menuItem = MenuUtil.createNewFromTo(menuItemTo, entityManager);
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(menuItem.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true)
    public void update(@Valid @RequestBody MenuItemTo menuItemTo, @PathVariable int id) {
        log.info("update menu item {} with id={}", menuItemTo, id);
        assureIdConsistent(menuItemTo, id);
        MenuItem menuItem = menuItemRepository.getByIdWithRestaurantAndDish(id).orElseThrow();
        MenuItem updated = MenuUtil.updateFromTo(menuItem, menuItemTo, entityManager);
        menuItemRepository.save(updated);
    }

    @GetMapping("/today/restaurant/{id}")
    @Cacheable
    public List<MenuItem> getByRestaurantId(@PathVariable(name = "id") int restaurantId) {
        return super.getByTodayByRestaurant(restaurantId);
    }

    @GetMapping("/today")
    @Cacheable
    public List<MenuItem> getByToday() {
        return super.getByToday();
    }
}