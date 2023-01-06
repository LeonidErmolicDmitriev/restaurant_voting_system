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
       ('Как дома'),
       ('Есть все');

INSERT INTO DISHES (name)
VALUES ('Philadelphia'),
       ('Spice herring'),
       ('Green tea'),
       ('Latte'),
       ('4 cheese'),
       ('Rancho'),
       ('Coca cola'),
       ('Sprite'),
       ('Борщ'),
       ('Плов'),
       ('Зама'),
       ('Картошка по-домашнему'),
       ('Компот');

INSERT INTO MENU_ITEMS(date, restaurant_id, dish_id, price)
VALUES (CURRENT_DATE, 1, 1, 15000),
       (CURRENT_DATE, 1, 3, 5000),
       (CURRENT_DATE, 2, 5, 50000),
       (CURRENT_DATE, 2, 7, 5000),
       (CURRENT_DATE, 3, 9, 30000),
       (CURRENT_DATE, 3, 10, 40000),
       (CURRENT_DATE, 3, 13, 10000),
       (CURRENT_DATE, 4, 9, 35000),
       (CURRENT_DATE, 4, 10, 35000),
       (CURRENT_DATE, 4, 7, 5500),

       (CURRENT_DATE - 1, 1, 2, 50000),
       (CURRENT_DATE - 1, 1, 4, 8000),
       (CURRENT_DATE - 1, 2, 6, 50000),
       (CURRENT_DATE - 1, 2, 8, 5000),
       (CURRENT_DATE - 1, 3, 11, 25000),
       (CURRENT_DATE - 1, 3, 12, 20000),
       (CURRENT_DATE - 1, 3, 13, 10000),
       (CURRENT_DATE - 1, 4, 1, 55000),
       (CURRENT_DATE - 1, 4, 4, 7500),

       (CURRENT_DATE + 1, 1, 2, 49000),
       (CURRENT_DATE + 1, 1, 4, 8300),
       (CURRENT_DATE + 1, 2, 6, 52000),
       (CURRENT_DATE + 1, 2, 8, 4000),
       (CURRENT_DATE + 1, 3, 11, 30000),
       (CURRENT_DATE + 1, 3, 12, 20000),
       (CURRENT_DATE + 1, 3, 13, 12000);
