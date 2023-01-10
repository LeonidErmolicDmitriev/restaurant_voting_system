INSERT INTO USERS (name, email, password)
VALUES ('User1', 'user1@yandex.ru', '{noop}password1'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('User2', 'user2@yandex.ru', '{noop}password2'),
       ('User3', 'user3@yandex.ru', '{noop}password3');

INSERT INTO USER_ROLES (role, user_id)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2),
       ('USER', 3),
       ('USER', 4);

INSERT INTO RESTAURANTS (name)
VALUES ('Sushi com'),
       ('Pizza hut'),
       ('Как дома');

INSERT INTO DISHES (name, restaurant_id)
VALUES ('Philadelphia', 1),
       ('Spice herring', 1),
       ('Green tea', 1),
       ('Latte', 1),
       ('4 cheese', 2),
       ('Rancho', 2),
       ('Coca cola', 2),
       ('Sprite', 2),
       ('Борщ', 3),
       ('Плов', 3),
       ('Зама', 3),
       ('Картошка по-домашнему', 3),
       ('Компот', 3),
       ('Тесто', 1);

INSERT INTO MENU_ITEMS(registered_date, restaurant_id, dish_id, price)
VALUES (CURRENT_DATE, 1, 1, 15000),
       (CURRENT_DATE, 1, 3, 5000),
       (CURRENT_DATE, 2, 5, 50000),
       (CURRENT_DATE, 2, 7, 5000),
       (CURRENT_DATE, 3, 9, 30000),
       (CURRENT_DATE, 3, 10, 40000),
       (CURRENT_DATE, 3, 13, 10000),

       (CURRENT_DATE - 1, 1, 2, 50000),
       (CURRENT_DATE - 1, 1, 4, 8000),
       (CURRENT_DATE - 1, 2, 6, 50000),
       (CURRENT_DATE - 1, 2, 8, 5000),
       (CURRENT_DATE - 1, 3, 11, 25000),
       (CURRENT_DATE - 1, 3, 12, 20000),
       (CURRENT_DATE - 1, 3, 13, 10000),

       (CURRENT_DATE + 1, 1, 2, 49000),
       (CURRENT_DATE + 1, 1, 4, 8300),
       (CURRENT_DATE + 1, 2, 6, 52000),
       (CURRENT_DATE + 1, 2, 8, 4000),
       (CURRENT_DATE + 1, 3, 11, 30000),
       (CURRENT_DATE + 1, 3, 12, 20000),
       (CURRENT_DATE + 1, 3, 13, 12000);

INSERT INTO VOTES(registered_date, user_id, restaurant_id)
VALUES (CURRENT_DATE, 1, 1),
       (CURRENT_DATE, 3, 3),
       (CURRENT_DATE, 4, 3),

       (CURRENT_DATE - 1, 1, 2),
       (CURRENT_DATE - 1, 2, 3),
       (CURRENT_DATE - 1, 3, 1),
       (CURRENT_DATE - 1, 4, 2),

       (CURRENT_DATE + 1, 1, 1),
       (CURRENT_DATE + 1, 2, 3),
       (CURRENT_DATE + 1, 3, 1),
       (CURRENT_DATE + 1, 4, 1);
