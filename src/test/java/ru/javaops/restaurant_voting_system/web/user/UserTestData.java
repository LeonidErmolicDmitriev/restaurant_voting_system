package ru.javaops.restaurant_voting_system.web.user;

import ru.javaops.restaurant_voting_system.model.Role;
import ru.javaops.restaurant_voting_system.model.User;
import ru.javaops.restaurant_voting_system.util.JsonUtil;
import ru.javaops.restaurant_voting_system.web.MatcherFactory;

import java.util.Collections;
import java.util.Date;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "password");

    public static final int USER_ID = 1;

    public static final int ADMIN_ID = 2;

    public static final int NOT_FOUND = 100;

    public static final String USER_MAIL = "user1@yandex.ru";

    public static final String ADMIN_MAIL = "admin@gmail.com";

    public static final User user = new User(USER_ID, "User1", USER_MAIL, "password1", Role.USER);

    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_MAIL, "admin", Role.ADMIN, Role.USER);

    public static final User user2 = new User(3, "User2", "user2@yandex.ru", "password2", Role.USER);

    public static final User user3 = new User(4, "User3", "user3@yandex.ru", "password3", Role.USER);

    public static User getNew() {
        return new User(null, "New", "new@gmail.com", "newPass", false, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        return new User(USER_ID, "UpdatedName", USER_MAIL, "newPass", false, new Date(), Collections.singleton(Role.ADMIN));
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
