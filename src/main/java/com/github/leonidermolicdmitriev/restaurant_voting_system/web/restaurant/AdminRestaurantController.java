package com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.RestaurantRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.github.leonidermolicdmitriev.restaurant_voting_system.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminRestaurantController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminRestaurantController extends AbstractRestaurantController {

    public static final String REST_URL = "/api/admin/restaurants";

    public AdminRestaurantController(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return super.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete restaurant by id {}", id);
        restaurantRepository.deleteExisted(id);
    }

    @GetMapping
    @Override
    public List<Restaurant> getAll() {
        return super.getAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create restaurant {}", restaurant);
        checkNew(restaurant);
        checkUniqueRestaurantName(restaurantRepository, restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update restaurant {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        checkUniqueRestaurantName(restaurantRepository, restaurant);
        restaurantRepository.save(restaurant);
    }
}