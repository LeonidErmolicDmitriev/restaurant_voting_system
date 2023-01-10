package com.github.leonidermolicdmitriev.restaurant_voting_system.repository;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Query("SELECT r FROM Restaurant r WHERE r.name = :name")
    Optional<Restaurant> getByName(@Param("name") String name);
}
