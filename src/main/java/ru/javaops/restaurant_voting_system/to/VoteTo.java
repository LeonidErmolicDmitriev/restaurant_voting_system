package ru.javaops.restaurant_voting_system.to;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import ru.javaops.restaurant_voting_system.model.Restaurant;
import ru.javaops.restaurant_voting_system.model.Vote;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VoteTo extends BaseTo {

    @NotNull
    @PositiveOrZero
    Restaurant restaurant;

    @NotNull
    LocalDate date;

    @PositiveOrZero
    int userId;

    public VoteTo(Integer id, Restaurant restaurant, LocalDate date, int userId) {
        super(id);
        this.restaurant = restaurant;
        this.date = date;
        this.userId = userId;
    }

    public VoteTo(Vote vote) {
        this(vote.getId(), vote.getRestaurant(), vote.getDate(), vote.getUserId());
    }
}
