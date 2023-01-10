package com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.MenuItem;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.MenuItemTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.JsonUtil;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.MenuUtil;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.AbstractControllerTest;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant.AdminRestaurantController;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.user.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.MenuItemRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.menu.MenuTestData.*;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT_1_ID + "/menu/";

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ITEM_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MenuUtil.createTo(menu_item_today1,
                        menu_item_today1.getRestaurant(), menu_item_today1.getDish())));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ITEM_ID_1))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuItemRepository.getByIdAndRestaurantId(MENU_ITEM_ID_1, RESTAURANT_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ITEM_ID_1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = UserTestData.USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ITEM_ID_1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MenuUtil.menuItemToTos(today_menu)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getTodayByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MenuUtil.menuItemToTos(today_menu)));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void getByWrongDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.of(2020, 1, 1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        updated.setId(MENU_ITEM_ID_1);
        MenuItemTo updatedTo = MenuUtil.createTo(updated, updated.getRestaurant(), updated.getDish());
        perform(MockMvcRequestBuilders.put(REST_URL + MENU_ITEM_ID_1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_ITEM_MATCHER.assertMatch(MenuUtil.getTo(
                        menuItemRepository.getByIdAndRestaurantId(MENU_ITEM_ID_1, RESTAURANT_1_ID)).orElseThrow(),
                MenuUtil.createTo(updated, updated.getRestaurant(), updated.getDish()));
    }

    @Test
    @WithUserDetails(value = UserTestData.ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = getNew();
        MenuItemTo newMenuItemTo = MenuUtil.createTo(newMenuItem, newMenuItem.getRestaurant(), newMenuItem.getDish());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItemTo)))
                .andExpect(status().isCreated());

        MenuItemTo created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItemTo.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItemTo);
        MENU_ITEM_MATCHER.assertMatch(MenuUtil.getTo(
                        menuItemRepository.getByIdAndRestaurantId(newId, RESTAURANT_1_ID)).orElseThrow(),
                newMenuItemTo);
    }
}