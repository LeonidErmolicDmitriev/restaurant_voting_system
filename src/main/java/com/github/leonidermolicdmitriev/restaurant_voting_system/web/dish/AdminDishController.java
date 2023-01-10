package com.github.leonidermolicdmitriev.restaurant_voting_system.web.dish;

import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.RestaurantRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.DishTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.DishUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.DishRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.leonidermolicdmitriev.restaurant_voting_system.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {
    private final RestaurantRepository restaurantRepository;

    static final String REST_URL = "/api/admin/restaurants/{restaurantId}/dishes";

    private final DishRepository dishRepository;

    public AdminDishController(DishRepository dishRepository,
                               RestaurantRepository restaurantRepository) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishTo> get(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("get dish by id {} with restaurantId = {}", id, restaurantId);
        return ResponseEntity.of(DishUtil.getTo(dishRepository.getByIdAndRestaurantId(id, restaurantId)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(allEntries = true, value = "menu_items")
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        log.info("delete dish by id {} with restaurantId = {}", id, restaurantId);
        dishRepository.getBodyIfExist(id, restaurantId);
        dishRepository.deleteExisted(id);
    }

    @GetMapping
    public List<DishTo> getAll(@PathVariable int restaurantId) {
        log.info("getAll dishes for restaurantId = {}", restaurantId);
        return DishUtil.dishesToTos(dishRepository.getAll(restaurantId));
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DishTo> createWithLocation(@Valid @RequestBody DishTo dishTo, @PathVariable int restaurantId) {
        log.info("create dish {} with restaurantId = {}", dishTo, restaurantId);

        checkNew(dishTo);
        Dish dish = DishUtil.createNewFromTo(dishTo, restaurantRepository.getById(restaurantId));
        checkUniqueDishName(dishRepository, dishTo, restaurantId);
        Dish created = dishRepository.save(dish);
        Map<String, Integer> urlParams = new HashMap<>();
        urlParams.put("restaurantId", restaurantId);
        urlParams.put("id", created.getId());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(urlParams).toUri();
        return ResponseEntity.created(uriOfNewResource).body(DishUtil.createTo(created));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update dish {} with id={} with restaurantId = {}", dishTo, id, restaurantId);
        assureIdConsistent(dishTo, id);
        Dish dish = dishRepository.getBodyIfExist(id, restaurantId);
        checkUniqueDishName(dishRepository, dishTo, restaurantId);
        DishUtil.updateFromTo(dish, dishTo);
        dishRepository.save(dish);
    }
}