INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
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

INSERT INTO DISHES (name, restaurant_id, price)
VALUES ('Philadelphia', 1, 15000),
       ('Spice herring', 1, 50000),
       ('Green tea', 1, 5000),
       ('Latte', 1, 8000),
       ('4 cheese', 2, 50000),
       ('Rancho', 2, 50000),
       ('Coca cola', 2, 5000),
       ('Sprite', 2, 5000),
       ('Борщ', 3, 30000),
       ('Плов', 3, 40000),
       ('Зама', 3, 25000),
       ('Картошка по-домашнему', 3, 20000),
       ('Компот', 3, 10000);

INSERT INTO MENU_ITEMS(date, restaurant_id, dish_id)
VALUES (CURRENT_DATE, 1, 1),
       (CURRENT_DATE, 1, 3),
       (CURRENT_DATE, 2, 5),
       (CURRENT_DATE, 2, 7),
       (CURRENT_DATE, 3, 9),
       (CURRENT_DATE, 3, 10),
       (CURRENT_DATE, 3, 13),

       (CURRENT_DATE-1, 1, 2),
       (CURRENT_DATE-1, 1, 4),
       (CURRENT_DATE-1, 2, 6),
       (CURRENT_DATE-1, 2, 8),
       (CURRENT_DATE-1, 3, 11),
       (CURRENT_DATE-1, 3, 12),
       (CURRENT_DATE-1, 3, 13),

       (CURRENT_DATE+1, 1, 2),
       (CURRENT_DATE+1, 1, 4),
       (CURRENT_DATE+1, 2, 6),
       (CURRENT_DATE+1, 2, 8),
       (CURRENT_DATE+1, 3, 11),
       (CURRENT_DATE+1, 3, 12),
       (CURRENT_DATE+1, 3, 13);
