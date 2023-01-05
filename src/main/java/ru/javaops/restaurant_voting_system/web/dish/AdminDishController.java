package ru.javaops.restaurant_voting_system.web.dish;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javaops.restaurant_voting_system.model.Dish;
import ru.javaops.restaurant_voting_system.repository.DishRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.javaops.restaurant_voting_system.util.validation.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class AdminDishController {

    static final String REST_URL = "/api/admin/dishes";

    private final DishRepository dishRepository;

    public AdminDishController(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @GetMapping("/{id}")
    public Dish get(@PathVariable int id) {
        log.info("get dish by id {}", id);
        return dishRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete dish by id {}", id);
        dishRepository.delete(id);
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("getAll dishes");
        return dishRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@Valid @RequestBody Dish dish) {
        log.info("create dish {}", dish);
        checkNew(dish);
        checkExistingName(dishRepository, dish);
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Dish dish, @PathVariable int id) {
        log.info("update dish {} with id={}", dish, id);
        assureIdConsistent(dish, id);
        checkExistingName(dishRepository, dish);
        dishRepository.save(dish);
    }
}