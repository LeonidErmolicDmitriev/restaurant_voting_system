package ru.javaops.restaurant_voting_system.web.vote;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurant_voting_system.model.Vote;
import ru.javaops.restaurant_voting_system.repository.VoteRepository;
import ru.javaops.restaurant_voting_system.to.VoteTo;
import ru.javaops.restaurant_voting_system.util.JsonUtil;
import ru.javaops.restaurant_voting_system.util.VoteUtil;
import ru.javaops.restaurant_voting_system.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurant_voting_system.web.vote.VoteTestData.*;
import static ru.javaops.restaurant_voting_system.web.user.UserTestData.*;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(new VoteTo(vote_user)));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + USER_VOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(new VoteTo(vote_user)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND_VOTE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUtil.votesToTos(votes_today)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by_date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VoteUtil.votesToTos(votes_today)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = getNewVote();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "?restaurantId="
                        + ADMIN_RESTAURANT_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isCreated());

        VoteTo created = VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTo newVoteTo = new VoteTo(newVote);
        VOTE_MATCHER.assertMatch(created, newVoteTo);
        VOTE_MATCHER.assertMatch(VoteUtil.getTo(voteRepository.getByIdWithUserAndRestaurant(newId)).orElseThrow(),
                newVoteTo);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void createDuplicate() throws Exception {
        Vote newVote = getNewVote();
        newVote.setUser(user);
        perform(MockMvcRequestBuilders.post(REST_URL + "?restaurantId="
                        + ADMIN_RESTAURANT_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateBeforeMaxTime() throws Exception {
        Vote updatedVote = getUpdatedVote();
        VoteRestController.setMaxUpdateTime(LocalTime.now().plusHours(1));
        perform(MockMvcRequestBuilders.put(REST_URL + "?restaurantId="
                        + ADMIN_RESTAURANT_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedVote)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VOTE_MATCHER.assertMatch(VoteUtil.getTo(voteRepository.getByIdWithUserAndRestaurant(USER_VOTE_ID))
                .orElseThrow(), new VoteTo(updatedVote));
        VoteRestController.setMaxUpdateTime(VoteRestController.MAX_UPDATE_TIME);
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void updateAfterMaxTime() throws Exception {
        Vote updatedVote = getUpdatedVote();
        VoteRestController.setMaxUpdateTime(LocalTime.now().minusHours(1));
        perform(MockMvcRequestBuilders.put(REST_URL + "?restaurantId="
                        + ADMIN_RESTAURANT_VOTE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedVote)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        VoteRestController.setMaxUpdateTime(VoteRestController.MAX_UPDATE_TIME);
    }
}