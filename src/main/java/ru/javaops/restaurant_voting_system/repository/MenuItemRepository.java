package ru.javaops.restaurant_voting_system.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting_system.model.MenuItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends BaseRepository<MenuItem> {
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"restaurant", "dish"})
    @Query("SELECT m FROM MenuItem m WHERE m.date =:date ORDER BY m.restaurant.name, m.dish.id")
    List<MenuItem> findAllByDate(@Param("date") LocalDate date);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"restaurant", "dish"})
    @Query("SELECT m FROM MenuItem m WHERE m.date =:date and m.restaurant.id = :restaurantId " +
            "ORDER BY m.restaurant.name, m.dish.id")
    List<MenuItem> getByDateAndRestaurantId(@Param("date") LocalDate date, @Param("restaurantId") int restaurantId);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"restaurant", "dish"})
    @Query("SELECT m FROM MenuItem m WHERE m.id = :id")
    Optional<MenuItem> getByIdWithRestaurantAndDish(@Param("id") int id);
}
