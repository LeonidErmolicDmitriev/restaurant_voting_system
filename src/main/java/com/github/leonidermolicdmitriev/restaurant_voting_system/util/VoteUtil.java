package com.github.leonidermolicdmitriev.restaurant_voting_system.util;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Vote;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.VoteTo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VoteUtil {
    public static List<VoteTo> votesToTos(List<Vote> votes) {
        return votes.stream().map(VoteUtil::createTo).collect(Collectors.toList());
    }

    public static Optional<VoteTo> getTo(Optional<Vote> optionalVote) {
        return optionalVote.map(VoteUtil::createTo);
    }

    public static VoteTo createTo(Vote vote) {
        return new VoteTo(vote.getId(), vote.getRestaurant().getId(), vote.getRegisteredDate(), vote.getUser().getId());
    }
}
