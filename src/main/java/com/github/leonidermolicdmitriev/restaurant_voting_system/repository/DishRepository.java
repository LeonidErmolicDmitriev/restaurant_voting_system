package com.github.leonidermolicdmitriev.restaurant_voting_system.repository;

import com.github.leonidermolicdmitriev.restaurant_voting_system.error.IllegalRequestDataException;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {
    @Query("SELECT d FROM Dish d WHERE d.id = :id and d.restaurant.id = :restaurantId")
    Optional<Dish> getByIdAndRestaurantId(@Param("id") int id, @Param("restaurantId") int restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId ORDER BY d.name")
    List<Dish> getAll(@Param("restaurantId") int restaurantId);

    default Dish getBodyIfExist(int id, int restaurantId) {
        return getByIdAndRestaurantId(id, restaurantId)
                .orElseThrow(() -> new IllegalRequestDataException("Dish with id=" + id +
                        " not found for restaurant with id=" + restaurantId));
    }

    @Query("SELECT d FROM Dish d WHERE d.name = :name and d.restaurant.id = :restaurantId")
    Optional<Dish> getByNameAndRestaurantId(@Param("name") String name, @Param("restaurantId") int restaurantId);
}

