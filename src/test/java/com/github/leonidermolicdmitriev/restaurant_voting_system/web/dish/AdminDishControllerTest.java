package com.github.leonidermolicdmitriev.restaurant_voting_system.web.dish;

import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Dish;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.DishTo;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.DishUtil;
import com.github.leonidermolicdmitriev.restaurant_voting_system.util.JsonUtil;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant.AdminRestaurantController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.DishRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.web.AbstractControllerTest;

import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.restaurant.RestaurantTestData.RESTAURANT_1_ID;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.user.UserTestData.ADMIN_MAIL;
import static com.github.leonidermolicdmitriev.restaurant_voting_system.web.user.UserTestData.USER_MAIL;

class AdminDishControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminRestaurantController.REST_URL + '/' + RESTAURANT_1_ID + "/dishes/";

    @Autowired
    private DishRepository dishRepository;

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DishTestData.DISH_1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                // https://jira.spring.io/browse/SPR-14472
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishUtil.createTo(DishTestData.dish1)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getNotFound() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + DishTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DishTestData.DISH_2_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertFalse(dishRepository.getByIdAndRestaurantId(DishTestData.DISH_2_ID, RESTAURANT_1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteNotFound() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + DishTestData.NOT_FOUND))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void update() throws Exception {
        Dish updated = DishTestData.getUpdated();
        updated.setId(DishTestData.DISH_2_ID);
        DishTo updatedTo = DishUtil.createTo(updated);
        perform(MockMvcRequestBuilders.put(REST_URL + DishTestData.DISH_2_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedTo)))
                .andDo(print())
                .andExpect(status().isNoContent());

        DishTestData.DISH_MATCHER.assertMatch(DishUtil.getTo(
                        dishRepository.getByIdAndRestaurantId(DishTestData.DISH_2_ID, RESTAURANT_1_ID)).orElseThrow(),
                updatedTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        DishTo newDishTo = DishUtil.createTo(newDish);
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDishTo)))
                .andExpect(status().isCreated());

        DishTo created = DishTestData.DISH_MATCHER.readFromJson(action);
        int newId = created.id();
        newDishTo.setId(newId);
        DishTestData.DISH_MATCHER.assertMatch(created, newDishTo);
        DishTestData.DISH_MATCHER.assertMatch(DishUtil.getTo(
                        dishRepository.getByIdAndRestaurantId(newId, RESTAURANT_1_ID)).orElseThrow(),
                newDishTo);
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DishTestData.DISH_MATCHER.contentJson(DishUtil.dishesToTos(DishTestData.dishes)));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createDuplicateName() throws Exception {
        Dish invalid = new Dish(null, DishTestData.dish1.getName(), DishTestData.dish1.getRestaurant());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(invalid)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateDuplicateName() throws Exception {
        Dish duplicate = new Dish(DishTestData.dish1);
        duplicate.setName(DishTestData.dish2.getName());
        perform(MockMvcRequestBuilders.put(REST_URL + DishTestData.DISH_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(duplicate)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getForbidden() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isForbidden());
    }
}