package com.github.leonidermolicdmitriev.restaurant_voting_system.repository;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.MenuItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface MenuItemRepository extends BaseRepository<MenuItem> {
    @EntityGraph(attributePaths = {"dish"})
    @Query("SELECT m FROM MenuItem m WHERE m.id =:id AND m.restaurant.id = :restaurantId")
    Optional<MenuItem> getByIdAndRestaurantId(@Param("id") int id,
                                              @Param("restaurantId") int restaurantId);

    @EntityGraph(attributePaths = {"dish"})
    @Query("SELECT m FROM MenuItem m WHERE m.registeredDate =:date and m.restaurant.id = :restaurantId " +
            "ORDER BY m.restaurant.name, m.dish.id")
    List<MenuItem> getByDateAndRestaurantId(@Param("date") LocalDate registeredDate,
                                            @Param("restaurantId") int restaurantId);
}
