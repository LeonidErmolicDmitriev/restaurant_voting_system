package ru.javaops.restaurant_voting_system.web.vote;

import ru.javaops.restaurant_voting_system.model.Vote;
import ru.javaops.restaurant_voting_system.to.VoteTo;
import ru.javaops.restaurant_voting_system.web.MatcherFactory;

import java.time.LocalDate;
import java.util.List;

import static ru.javaops.restaurant_voting_system.web.user.UserTestData.*;
import static ru.javaops.restaurant_voting_system.web.restaurant.RestaurantTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<VoteTo> VOTE_MATCHER =
            MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);

    public static final int USER_VOTE_ID = 1;

    public static final int NOT_FOUND_VOTE_ID = 100;

    public static final int ADMIN_RESTAURANT_VOTE_ID = 4;

    public static final Vote vote_user = new Vote(USER_VOTE_ID, user, restaurant1, LocalDate.now());

    public static final Vote vote_user2 = new Vote(2, user2, restaurant3, LocalDate.now());

    public static final Vote vote_user3 = new Vote(3, user3, restaurant3, LocalDate.now());

    public static final List<Vote> votes_today = List.of(vote_user, vote_user2, vote_user3);

    public static Vote getNewVote() {
        return new Vote(null, admin, restaurant4, LocalDate.now());
    }

    public static Vote getUpdatedVote() {
        return new Vote(USER_VOTE_ID, user, restaurant4, LocalDate.now());
    }
}
