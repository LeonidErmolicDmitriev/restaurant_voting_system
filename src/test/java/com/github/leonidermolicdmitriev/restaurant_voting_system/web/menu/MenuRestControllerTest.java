package com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu;

import com.github.leonidermolicdmitriev.restaurant_voting_system.util.MenuUtil;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.AbstractControllerTest;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant.RestaurantRestController;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu.MenuTestData.*;
import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantRestController.REST_URL + '/' + RESTAURANT_1_ID + "/menu/";

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ITEM_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MenuUtil.menuItemToTos(today_menu)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getTodayByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MenuUtil.menuItemToTos(today_menu)));
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void getByWrongDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.of(2020, 1, 1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(Collections.emptyList()));
    }
}