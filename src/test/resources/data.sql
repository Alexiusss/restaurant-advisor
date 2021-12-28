DELETE
FROM user_role;
DELETE
FROM users;
DELETE
FROM restaurants;

INSERT INTO users (id, activation_code, active, email, first_name, last_name, password)
VALUES (1, null, true, 'admin@gmail.com', 'Admin', 'AdminLast',
        '{bcrypt}$2a$08$fcvC7plZPHirDqsgEplSgODGaVwXlWEJSCZFXbiNsYJqbpPHzz3n2'),
       (2, null, true, 'user@yandex.ru', 'User', 'UserLast',
        '{bcrypt}$2a$08$fcvC7plZPHirDqsgEplSgODGaVwXlWEJSCZFXbiNsYJqbpPHzz3n2');

INSERT INTO user_role (role, user_id)
VALUES ('ADMIN', 1),
       ('USER', 1),
       ('USER', 2);

INSERT INTO restaurants(id, cuisine, name, menu)
VALUES (3, 'seafood', 'restaurant1', 'www.menu1.com'),
       (4, 'vegan', 'restaurant2', 'www.menu2.com'),
       (5, 'italian', 'cafe', 'www.menu3.com'),
       (6, 'italian', 'bar', 'www.menu4.com');

INSERT INTO contacts(restaurant_id, address, email, phone_number, website)
VALUES (3, 'restaurant1 address', 'restaurant1@email', +11111111, 'www.restaurant1.com'),
       (5, 'cafe address', 'cafe@email', +3333333, 'www.cafe.com'),
       (6, 'bar address', 'bar@email', +4444444, 'www.bar.com');

INSERT INTO reviews(id, comment, date, rating, title, restaurant_id, user_id, active, filename)
VALUES (20, 'Positive review', now(), 5, 'Positive title', 3, 2, false, null),
       (21, 'Review for deleting', now(), 2, 'Delete this', 5, 2, false, null);

ALTER sequence hibernate_sequence RESTART WITH 33;