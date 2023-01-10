package com.github.leonidermolicdmitriev.restaurant_voting_system.repository;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Vote;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {
    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v WHERE v.registeredDate = :date ORDER BY v.restaurant.name, v.user.id")
    List<Vote> getAllByDate(@Param("date") LocalDate registeredDate);

    @Query("SELECT v FROM Vote v WHERE v.registeredDate = :date AND v.user.id = :userId")
    Optional<Vote> getByDateAndUserId(@Param("date") LocalDate registeredDate, @Param("userId") Integer userId);

    @EntityGraph(attributePaths = {"restaurant"})
    @Query("SELECT v FROM Vote v WHERE v.id = :id")
    Optional<Vote> getByIdWithRestaurant(@Param("id") Integer id);
}
