package ru.javaops.restaurant_voting_system.web.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javaops.restaurant_voting_system.model.MenuItem;
import ru.javaops.restaurant_voting_system.repository.MenuItemRepository;
import ru.javaops.restaurant_voting_system.to.MenuItemTo;
import ru.javaops.restaurant_voting_system.util.JsonUtil;
import ru.javaops.restaurant_voting_system.web.AbstractControllerTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.restaurant_voting_system.web.menu.MenuTestData.*;
import static ru.javaops.restaurant_voting_system.web.user.UserTestData.ADMIN_MAIL;
import static ru.javaops.restaurant_voting_system.web.user.UserTestData.USER_MAIL;

class AdminMenuControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminMenuController.REST_URL + '/';

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MENU_ITEM_ID_1))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(menu_item_today1));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ITEM_ID_1))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(menuItemRepository.findById(MENU_ITEM_ID_1).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
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
    @WithUserDetails(value = USER_MAIL)
    void deleteForbidden() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MENU_ITEM_ID_1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getToday() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(today_menu));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by_date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(today_menu));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getTodayByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "today/restaurant/" + restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(restaurant_today_menu));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getByWrongDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by_date?date=" + DateTimeFormatter
                .ofPattern("yyyy-MM-dd").format(LocalDate.of(2020, 1, 1))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getTodayByWrongRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "today/restaurant/100"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(Collections.emptyList()));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        MenuItem updated = getUpdated();
        MenuItemTo updatedTo = new MenuItemTo(updated);
        perform(MockMvcRequestBuilders.put(REST_URL + MENU_ITEM_ID_2)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());
        updated.setId(MENU_ITEM_ID_2);
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getById(MENU_ITEM_ID_2), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        MenuItem newMenuItem = getNew();
        MenuItemTo newMenuItemTo = new MenuItemTo(newMenuItem);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenuItemTo)))
                .andExpect(status().isCreated());

        MenuItem created = MENU_ITEM_MATCHER.readFromJson(action);
        int newId = created.id();
        newMenuItem.setId(newId);
        MENU_ITEM_MATCHER.assertMatch(created, newMenuItem);
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.getById(newId), newMenuItem);
    }
}