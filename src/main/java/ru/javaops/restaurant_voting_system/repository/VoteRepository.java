package ru.javaops.restaurant_voting_system.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.restaurant_voting_system.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends BaseRepository<Vote>{
    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"restaurant", "user"})
    @Query("SELECT v FROM Vote v WHERE v.date = :date ORDER BY v.restaurant.name, v.user.id")
    List<Vote> getAllByDate(@Param("date")LocalDate date);

    @Transactional(readOnly = true)
    @EntityGraph(attributePaths = {"restaurant", "user"})
    @Query("SELECT v FROM Vote v WHERE v.date = :date and v.user.id = :userId")
    Optional<Vote> getByDateAndUserId(@Param("date")LocalDate date, @Param("userId")Integer userId);

}
