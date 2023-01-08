package ru.javaops.restaurant_voting_system.util;

import ru.javaops.restaurant_voting_system.model.Vote;
import ru.javaops.restaurant_voting_system.to.VoteTo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VoteUtil {
    public static List<VoteTo> votesToTos(List<Vote> votes) {
        List<VoteTo> tos = new ArrayList<>();
        for (Vote vote : votes) {
            tos.add(new VoteTo(vote));
        }
        return tos;
    }

    public static Optional<VoteTo> getTo(Optional<Vote> optionalVote) {
        Optional<VoteTo> resultTo;
        if (optionalVote.isPresent()) {
            VoteTo to = new VoteTo(optionalVote.get());
            resultTo = Optional.of(to);
        } else {
            resultTo = Optional.empty();
        }
        return resultTo;
    }
}
