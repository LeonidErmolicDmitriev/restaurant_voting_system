package com.github.leonidermolicdmitriev.restaurant_voting_system.to;

import java.time.LocalDate;

public class VoteTo extends BaseTo {

    int restaurantId;

    LocalDate registeredDate;

    int userId;

    public VoteTo(Integer id, int restaurantId, LocalDate registeredDate, int userId) {
        super(id);
        this.restaurantId = restaurantId;
        this.registeredDate = registeredDate;
        this.userId = userId;
    }
}
