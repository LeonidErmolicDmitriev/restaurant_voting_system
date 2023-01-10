package com.github.leonidermolicdmitriev.restaurant_voting_system.web.vote;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Vote;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.AbstractControllerTest;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.VoteRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.VoteTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.VoteUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = VoteRestController.REST_URL + '/';

    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getTodayByUser() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "profile"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteUtil.createTo(VoteTestData.vote_user)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VoteTestData.USER_VOTE_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteUtil.createTo(VoteTestData.vote_user)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + VoteTestData.NOT_FOUND_VOTE_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteUtil.votesToTos(VoteTestData.votes_today)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VoteTestData.VOTE_MATCHER.contentJson(VoteUtil.votesToTos(VoteTestData.votes_today)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Vote newVote = VoteTestData.getNewVote();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + "profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(VoteTestData.ADMIN_RESTAURANT_VOTE_ID)))
                .andExpect(status().isCreated());

        VoteTo created = VoteTestData.VOTE_MATCHER.readFromJson(action);
        int newId = created.id();
        newVote.setId(newId);
        VoteTo newVoteTo = VoteUtil.createTo(newVote);
        VoteTestData.VOTE_MATCHER.assertMatch(created, newVoteTo);
        VoteTestData.VOTE_MATCHER.assertMatch(VoteUtil.getTo(voteRepository.getByIdWithRestaurant(newId)).orElseThrow(),
                newVoteTo);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void createDuplicate() throws Exception {
        Vote newVote = VoteTestData.getNewVote();
        newVote.setUser(UserTestData.user);
        perform(MockMvcRequestBuilders.post(REST_URL + "profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(VoteTestData.ADMIN_RESTAURANT_VOTE_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateBeforeMaxTime() throws Exception {
        Vote updatedVote = VoteTestData.getUpdatedVote();
        LocalTime initMaxUpdateTime = VoteRestController.maxUpdateTime;
        VoteRestController.setMaxUpdateTime(LocalTime.MAX);
        perform(MockMvcRequestBuilders.put(REST_URL + "profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(VoteTestData.ADMIN_RESTAURANT_VOTE_ID)))
                .andDo(print())
                .andExpect(status().isNoContent());
        VoteTestData.VOTE_MATCHER.assertMatch(VoteUtil.getTo(voteRepository.getByIdWithRestaurant(VoteTestData.USER_VOTE_ID))
                .orElseThrow(), VoteUtil.createTo(updatedVote));
        VoteRestController.setMaxUpdateTime(initMaxUpdateTime);
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void updateAfterMaxTime() throws Exception {
        LocalTime initMaxUpdateTime = VoteRestController.maxUpdateTime;
        VoteRestController.setMaxUpdateTime(LocalTime.MIN);
        perform(MockMvcRequestBuilders.put(REST_URL + "profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.valueOf(VoteTestData.ADMIN_RESTAURANT_VOTE_ID)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
        VoteRestController.setMaxUpdateTime(initMaxUpdateTime);
    }
}