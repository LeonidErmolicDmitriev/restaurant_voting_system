package com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class RestaurantRestController extends AbstractRestaurantController {

    public static final String REST_URL = "/api/restaurants";

    public RestaurantRestController(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping
    @Override
    public List<Restaurant> getAll() {
        return super.getAll();
    }
}