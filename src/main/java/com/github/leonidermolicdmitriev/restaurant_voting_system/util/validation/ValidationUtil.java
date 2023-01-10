package com.github.leonidermolicdmitriev.restaurant_voting_system.util.validation;

import com.github.leonidermolicdmitriev.restaurant_voting_system.HasId;
import com.github.leonidermolicdmitriev.restaurant_voting_system.error.IllegalRequestDataException;
import com.github.leonidermolicdmitriev.restaurant_voting_system.model.Restaurant;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.DishRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.MenuItemRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.repository.RestaurantRepository;
import com.github.leonidermolicdmitriev.restaurant_voting_system.to.DishTo;
import lombok.experimental.UtilityClass;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.lang.NonNull;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkModification(int count, int id) {
        if (count == 0) {
            throw new IllegalRequestDataException("Entity with id=" + id + " not found");
        }
    }

    //  https://stackoverflow.com/a/65442410/548473
    @NonNull
    public static Throwable getRootCause(@NonNull Throwable t) {
        Throwable rootCause = NestedExceptionUtils.getRootCause(t);
        return rootCause != null ? rootCause : t;
    }

    public static void checkUniqueDishName(DishRepository repository, DishTo dish, int restaurantId) {
        String name = dish.getName();
        if (repository.getByNameAndRestaurantId(name, restaurantId).isPresent()) {
            throw new IllegalRequestDataException("Dish with name=" + name
                    + " already exist for restaurant with id=" + restaurantId);
        }
    }

    public static void checkUniqueRestaurantName(RestaurantRepository repository, Restaurant restaurant) {
        String name = restaurant.getName();
        if (repository.getByName(name).isPresent()) {
            throw new IllegalRequestDataException("Restaurant with name=" + name + " already exist");
        }
    }

    public void checkIfMenuItemExist(MenuItemRepository repository, int id, int restaurantId) {
        if (repository.getByIdAndRestaurantId(id, restaurantId).isEmpty()) {
            throw new IllegalRequestDataException("Menu item with id=" + id
                    + " not found for restaurant with id=" + restaurantId);
        }
    }
}