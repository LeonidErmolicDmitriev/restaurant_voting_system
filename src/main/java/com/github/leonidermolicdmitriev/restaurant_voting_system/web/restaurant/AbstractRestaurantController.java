package com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
public abstract class AbstractRestaurantController {

    protected final RestaurantRepository restaurantRepository;

    protected AbstractRestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> getAll() {
        log.info("getAll restaurants");
        return restaurantRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get restaurant by id {}", id);
        return ResponseEntity.of(restaurantRepository.findById(id));
    }
}